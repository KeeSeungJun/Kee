 // 임시 비밀번호 - 실제로는 서버에서 확인해야합니다@@@@@@@@@@@@@@@@@@
    const CORRECT_PASSWORD = "qwerty";
// 임시 비밀번호 - 실제로는 서버에서 확인해야합니다@@@@@@@@@@@@@@@@@@

    const openModalBtn = document.getElementById('openModalBtn');
    const modalBg = document.getElementById('modalBg');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const togglePasswordBtn = document.getElementById('togglePasswordBtn');
    const passwordInput = document.getElementById('passwordInput');
    const modalMessage = document.getElementById('modalMessage');
    const passwordForm = document.getElementById('passwordForm');
    const withdrawalForm = document.getElementById('withdrawalForm');

    openModalBtn.addEventListener('click', () => {
    modalMessage.textContent = '';
    modalMessage.className = 'message';
    passwordInput.value = '';
    passwordInput.type = 'password';
    togglePasswordBtn.innerHTML = '<i class="fa-solid fa-eye"></i>';
    modalBg.classList.add('active');
    passwordInput.focus();
});


    closeModalBtn.addEventListener('click', () => {
    modalBg.classList.remove('active');
});

    togglePasswordBtn.addEventListener('click', () => {
    if (passwordInput.type === 'password') {
    passwordInput.type = 'text';
    togglePasswordBtn.innerHTML = '<i class="fa-solid fa-eye-slash"></i>';
} else {
    passwordInput.type = 'password';
    togglePasswordBtn.innerHTML = '<i class="fa-solid fa-eye"></i>';
}
});

    passwordForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const enteredPassword = passwordInput.value.trim();

    if (!enteredPassword) {
    modalMessage.textContent = '비밀번호를 입력해주세요.';
    modalMessage.className = 'message error';
    return;
}

    if (enteredPassword === CORRECT_PASSWORD) {
    modalMessage.textContent = '비밀번호 확인 성공! 계정 탈퇴가 진행됩니다.';
    modalMessage.className = 'message success';

    setTimeout(() => {
    modalBg.classList.remove('active');

    let hiddenPasswordInput = withdrawalForm.querySelector('input[name="password"]');
    if (!hiddenPasswordInput) {
    hiddenPasswordInput = document.createElement('input');
    hiddenPasswordInput.type = 'hidden';
    hiddenPasswordInput.name = 'password';
    withdrawalForm.appendChild(hiddenPasswordInput);
}
    hiddenPasswordInput.value = enteredPassword;
    withdrawalForm.submit();
}, 1500);
} else {
    modalMessage.textContent = '비밀번호가 틀렸습니다. 다시 시도해주세요.';
    modalMessage.className = 'message error';
    passwordInput.value = '';
    passwordInput.focus();
}
});

    document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && modalBg.classList.contains('active')) {
    modalBg.classList.remove('active');
}
});

    // 계정 탈퇴시 login.html로 이동하는 코드입니다. 서버와 연동필요 해요@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @PostMapping("/accDelConfirm")
    // public String processAccountDeletion(HttpSession session, ...) {
    //     // 1. 계정 삭제 처리
    //     // 2. 세션 무효화
    //     session.invalidate();
    //     // 3. 로그인 페이지로 리디렉션
    //     return "redirect:/login.html";
    // }