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
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                credentials: 'same-origin',
                body: JSON.stringify(payload)
            });

            let loginData = null;
            try { loginData = await loginResp.json(); } catch (e) {}
            console.log('[login] /api/login =>', loginResp.status, loginData);

            if (!loginResp.ok || !loginData || loginData.code !== 200) {
                alert(`[ERROR] ${(loginData && loginData.message) || '로그인 실패'}`);
                return;
            }

            const meResp = await fetch('/api/user/me', {
                method: 'GET',
                headers: { 'Accept': 'application/json' },
                credentials: 'same-origin'
            });
            console.log('[login] /api/user/me status:', meResp.status);

            if (!meResp.ok) {
                alert('[ERROR] 사용자 정보를 확인할 수 없습니다. 다시 로그인 해주세요.');
                return;
            }

            const me = await meResp.json();
            console.log('[login] me payload:', me);

            const rawRole =
                me.userGroupId ??
                me.user_group_id ??
                '';
            const role = String(rawRole).trim().toUpperCase();
            console.log('[login] resolved role:', role);

            const next = role === 'ADMIN' ? '/adminmain' : '/main';
            window.location.href = next;

        } catch (err) {
            console.error('Login error:', err);
            alert(`[ERROR] ${err}`);
        } finally {
            if (submitBtn) submitBtn.disabled = false;
        }
    });
});
