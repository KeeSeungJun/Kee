function openLogoutModal() {
    const modalBg = document.getElementById('logoutModalBg');
    if (modalBg) {
        modalBg.classList.add('active');
    }
}

async function handleLogout() {
    try {
        const response = await fetch('/api/logout', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        const result = await response.json();

        if (response.ok && result.code === 200) {
            window.location.href = '/login';
        } else {
            alert(`[오류] 로그아웃에 실패했습니다: ${result.message || '서버 오류'}`);
            closeLogoutModal();
        }
    } catch (error) {
        console.error('Logout Error:', error);
        alert('서버와 통신 중 오류가 발생했습니다.');
        closeLogoutModal();
    }
}

function closeLogoutModal() {
    const modalBg = document.getElementById('logoutModalBg');
    if (modalBg) {
        modalBg.classList.remove('active');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const confirmBtn = document.getElementById('confirmLogoutBtn');
    const cancelBtn = document.getElementById('cancelLogoutBtn');
    const modalBg = document.getElementById('logoutModalBg');

    if (confirmBtn) {
        confirmBtn.addEventListener('click', handleLogout);
    }

    if (cancelBtn) {
        cancelBtn.addEventListener('click', closeLogoutModal);
    }

    if (modalBg) {
        modalBg.addEventListener('click', (e) => {
            if (e.target === modalBg) {
                closeLogoutModal();
            }
        });
    }
});