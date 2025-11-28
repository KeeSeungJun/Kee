// [임시] 테스트용 비밀번호
const CORRECT_PASSWORD = "qwerty";

document.addEventListener('DOMContentLoaded', () => {
    const openModalBtn = document.getElementById('openModalBtn');
    const modalBg = document.getElementById('modalBg');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const togglePasswordBtn = document.getElementById('togglePasswordBtn');
    const passwordInput = document.getElementById('passwordInput');
    const finalDeleteBtn = document.getElementById('finalDeleteBtn');
    const withdrawalForm = document.getElementById('withdrawalForm');
    const hiddenPassword = document.getElementById('hiddenPassword');

    // 모달 열기
    if (openModalBtn) {
        openModalBtn.addEventListener('click', () => {
            passwordInput.value = '';
            modalBg.classList.add('active');
            passwordInput.focus();
        });
    }

    // 모달 닫기
    if (closeModalBtn) closeModalBtn.addEventListener('click', closeModal);

    // 배경 클릭 시 닫기
    modalBg.addEventListener('click', (e) => {
        if (e.target === modalBg) closeModal();
    });

    function closeModal() {
        modalBg.classList.remove('active');
    }

    // 비밀번호 보기 토글
    if (togglePasswordBtn) {
        togglePasswordBtn.addEventListener('click', () => {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            togglePasswordBtn.innerHTML = type === 'password' ? '<i class="fa-solid fa-eye"></i>' : '<i class="fa-solid fa-eye-slash"></i>';
        });
    }

    // 탈퇴 버튼 클릭 (검증 로직)
    if (finalDeleteBtn) {
        finalDeleteBtn.addEventListener('click', () => {
            const enteredPassword = passwordInput.value.trim();

            if (!enteredPassword) {
                showToast("비밀번호를 입력해주세요.", "error");
                return;
            }

            // [백엔드 연동 필요] 실제로는 서버로 비밀번호를 보내서 검증해야 함
            if (enteredPassword === CORRECT_PASSWORD) {
                showToast("확인되었습니다. 탈퇴를 진행합니다...", "success");

                // 폼에 비밀번호 담아서 제출
                hiddenPassword.value = enteredPassword;

                setTimeout(() => {
                    withdrawalForm.submit();
                }, 1000);
            } else {
                showToast("비밀번호가 일치하지 않습니다.", "error");
                passwordInput.value = '';
                passwordInput.focus();
            }
        });
    }
});