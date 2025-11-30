// 모달 제어
function openSignupModal() {
    document.getElementById('signupModal').style.display = 'flex';
}

function closeSignupModal() {
    document.getElementById('signupModal').style.display = 'none';
}

// 비밀번호 토글 기능
function togglePassword(inputId, icon) {
    const input = document.getElementById(inputId);
    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove("fa-eye");
        icon.classList.add("fa-eye-slash");
    } else {
        input.type = "password";
        icon.classList.remove("fa-eye-slash");
        icon.classList.add("fa-eye");
    }
}

// 로그인 로직
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');
    if (!form) return;

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) submitBtn.disabled = true;

        try {
            const payload = {
                user_id: document.getElementById('user-id').value,
                passwd:  document.getElementById('passwd').value
            };

            const loginResp = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            let loginData = null;
            try { loginData = await loginResp.json(); } catch (e) {}

            if (!loginResp.ok || !loginData || loginData.code !== 200) {
                // [수정] Alert -> Toast
                showToast(`[로그인 실패] ${(loginData && loginData.message) || '아이디 또는 비밀번호를 확인해주세요.'}`, 'error');
                if (submitBtn) submitBtn.disabled = false;
                return;
            }

            // 로그인 성공 시 사용자 정보 확인하여 리다이렉트
            const meResp = await fetch('/api/user/me', {
                method: 'GET',
                headers: { 'Accept': 'application/json' }
            });

            if (!meResp.ok) {
                showToast('사용자 정보를 확인할 수 없습니다. 다시 시도해주세요.', 'error');
                return;
            }

            const me = await meResp.json();
            const role = String(me.userGroupId || '').trim().toUpperCase();
            const next = role === 'ADMIN' ? '/adminmain' : '/main';

            window.location.href = next;

        } catch (err) {
            console.error('Login error:', err);
            showToast('서버 통신 중 오류가 발생했습니다.', 'error');
            if (submitBtn) submitBtn.disabled = false;
        }
    });

    const modal = document.getElementById('signupModal');
    window.addEventListener('click', (e) => {
        if (e.target === modal) closeSignupModal();
    });
});