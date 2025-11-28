document.addEventListener('DOMContentLoaded', () => {
    // 1. 사용자 정보 가져오기
    fetchUserProfile();

    // 2. 모달 로직
    const logoutBtn = document.getElementById('logoutBtn');
    const modalBg = document.getElementById('logoutModalBg');
    const cancelBtn = document.getElementById('cancelLogoutBtn');
    const confirmBtn = document.getElementById('confirmLogoutBtn');

    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            modalBg.classList.add('active');
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener('click', closeModal);
    }

    if (modalBg) {
        modalBg.addEventListener('click', (e) => {
            if (e.target === modalBg) {
                closeModal();
            }
        });
    }

    function closeModal() {
        if (modalBg) modalBg.classList.remove('active');
    }

    // 3. 로그아웃 처리
    if (confirmBtn) {
        confirmBtn.addEventListener('click', async () => {
            try {
                const response = await fetch('/api/logout', {
                    method: 'GET',
                    headers: { 'Accept': 'application/json' }
                });

                const result = await response.json();

                if (response.ok && result.code === 200) {
                    window.location.href = '/login';
                } else {
                    // [수정] Alert -> Toast
                    showToast(`로그아웃 실패: ${result.message || '오류가 발생했습니다'}`, 'error');
                    closeModal();
                }
            } catch (error) {
                console.error('Logout Error:', error);
                // [수정] Alert -> Toast
                showToast('서버와 통신 중 오류가 발생했습니다.', 'error');
                closeModal();
            }
        });
    }
});

async function fetchUserProfile() {
    try {
        const response = await fetch('/api/user/me', {
            method: 'GET',
            headers: { 'Accept': 'application/json' }
        });

        if (response.ok) {
            const user = await response.json();

            const nameEl = document.getElementById('profile-name');
            const idEl = document.getElementById('profile-id');

            if (nameEl && user.userName) nameEl.innerText = user.userName;
            if (idEl && user.userId) idEl.innerText = user.userId;

        } else {
            console.warn('사용자 정보를 가져오지 못했습니다.');
        }
    } catch (error) {
        console.error('프로필 조회 중 오류 발생:', error);
    }
}