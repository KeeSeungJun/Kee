// =================================================================================
//
// 1. 인증번호 전송 (API: /api/send-verification-code)
//    - 클라이언트: phone을 전송
//    - 서버: 해당 phone으로 인증번호 전송 후, Redis/DB에 [phone, code, 만료시간] 저장
//
// 2. 인증번호 확인 (API: /api/verify-code)
//    - 클라이언트: phone, code를 전송
//    - 서버: 저장된 code와 비교 후 일치 여부 반환 (인증 성공 시 isPhoneVerified 상태 변경)
//    - MOCK_CORRECT_CODE 제거 (연동 완료 파악 후 임시 인증번호 제거 예정)
//
// 3. 사용자 정보 일치 확인 (API: /api/check-user-identity)
//    - 클라이언트: userId, userName, phone을 전송 (인증 완료 후)
//    - 서버: DB에서 해당 세 가지 정보(아이디/이름/전화번호)가 모두 일치하는 사용자 존재 여부 확인 및 반환
//
// 4. 비밀번호 재설정 (API: /api/reset-password)
//    - 클라이언트: userId, newPassword를 전송
//    - 서버: 1단계에서 인증된 userId가 맞는지 확인 후, newPassword로 DB 업데이트
//
// =================================================================================

let timerInterval;
let remainingTime = 180;
let isPhoneVerified = false; // 전화번호 인증 완료 상태 (임시)

function toggleVisibility(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);

    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// 뒤로가기 버튼
function goBackToStep1() {
    const step1 = document.getElementById('step1');
    const step2 = document.getElementById('step2');

    if (step2 && step2.style.display === 'block') {
        step2.style.display = 'none';
        step1.style.display = 'block';

        clearInterval(timerInterval);
        remainingTime = 180;
        document.getElementById('timer').textContent = "03:00";
        isPhoneVerified = false;

        document.getElementById('verification-code').disabled = true;
        document.getElementById('verification-code').value = '';
        document.getElementById('verify-code-btn').disabled = true;
        document.getElementById('send-code-btn').disabled = true;

        checkStep1Validation();
        handlePhoneInput();
    } else {
        window.location.href = '/login';
    }
}


// 전화번호 입력 및 인증
function handlePhoneInput() {
    const phoneInput = document.getElementById('phone');
    phoneInput.value = phoneInput.value.replace(/[^0-9]/g, '').slice(0, 11);

    const sendBtn = document.getElementById('send-code-btn');
    sendBtn.disabled = phoneInput.value.length !== 11 || isPhoneVerified;

    checkStep1Validation();
}

function startVerification() {
    const phone = document.getElementById('phone').value.trim();
    const sendBtn = document.getElementById('send-code-btn');
    const codeInput = document.getElementById('verification-code');
    const verifyBtn = document.getElementById('verify-code-btn');

    if (phone.length !== 11) {
        alert("전화번호를 11자리로 정확히 입력해주세요.");
        return;
    }

    // 백앤드 통신 필요 (인증번호 전송 API)
    console.log(`[DEV_LOG] Sending verification code to ${phone}... (API call to /api/send-code)`);

    clearInterval(timerInterval);
    remainingTime = 180;
    updateTimerDisplay();

    codeInput.disabled = false;
    verifyBtn.disabled = true;
    sendBtn.disabled = true;

    timerInterval = setInterval(() => {
        remainingTime--;
        updateTimerDisplay();

        if (remainingTime <= 0) {
            clearInterval(timerInterval);
            document.getElementById('timer').textContent = "시간초과";
            sendBtn.disabled = false;
            verifyBtn.disabled = true;
        }
    }, 1000);

    // 임시 인증번호 데이터@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    alert("인증번호가 전송되었습니다. 3분 내로 입력해주세요. (임시 코드: 123456)");
    codeInput.focus();
}

function handleVerificationInput() {
    const codeInput = document.getElementById('verification-code');
    codeInput.value = codeInput.value.replace(/[^0-9]/g, '').slice(0, 6);

    const verifyBtn = document.getElementById('verify-code-btn');
    verifyBtn.disabled = codeInput.value.length !== 6 || remainingTime <= 0;
}

function updateTimerDisplay() {
    const minutes = String(Math.floor(remainingTime / 60)).padStart(2, '0');
    const seconds = String(remainingTime % 60).padStart(2, '0');
    document.getElementById('timer').textContent = `${minutes}:${seconds}`;
}

function verifyCode() {
    const code = document.getElementById('verification-code').value;

    if (code.length !== 6) {
        alert("인증번호 6자리를 입력해주세요.");
        return;
    }

    // 백앤드 통신 필요 (인증번호 전송 API)
    console.log(`[DEV_LOG] Verifying code ${code}... (API call to /api/verify-code)`);

    // 임시 인증번호 데이터@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    const MOCK_CORRECT_CODE = "123456";

    if (code === MOCK_CORRECT_CODE) {
        clearInterval(timerInterval);
        isPhoneVerified = true;

        document.getElementById('timer').textContent = "인증 완료";
        document.getElementById('verification-code').disabled = true;
        document.getElementById('verify-code-btn').disabled = true;

        checkStep1Validation();

        alert("인증에 성공했습니다.");
    } else {
        alert("인증번호가 일치하지 않습니다. 다시 시도해주세요.");
    }
}

// 폼 유효, 단계 전환
function checkStep1Validation() {
    const userId = document.getElementById('user-id').value.trim();
    const userName = document.getElementById('user-name').value.trim();
    const nextBtn = document.getElementById('next-step-btn');
    const isFieldsFilled = userId.length > 0 && userName.length > 0;
    const isValid = isFieldsFilled && isPhoneVerified;

    nextBtn.disabled = !isValid;
}


document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('#user-id, #user-name').forEach(input => {
        input.addEventListener('input', checkStep1Validation);
    });

    handlePhoneInput();
    checkStep1Validation();

    const nextStepBtn = document.getElementById('next-step-btn');

    if (nextStepBtn) {
        nextStepBtn.addEventListener('click', async function() {
            if (nextStepBtn.disabled) return;

            const userId = document.getElementById('user-id').value.trim();
            const userName = document.getElementById('user-name').value.trim();
            const phone = document.getElementById('phone').value.trim();

            // 백앤드 통신 필요 (인증번호 전송 API)
            // 여기서 아이디/이름/전화번호 일치 여부를 확인하는 API 호출이 필요해요 @@
            console.log(`[DEV_LOG] Checking identity: ${userId}, ${userName}, ${phone}... (API call to /api/check-user-identity)`);

            const isIdentityValid = true;

            if (isIdentityValid) {
                document.getElementById('step1').style.display = 'none';
                document.getElementById('step2').style.display = 'block';
                document.getElementById('new-password').focus();
            } else {
                alert("입력하신 아이디, 이름, 전화번호가 일치하는 사용자가 없습니다.");
            }
        });
    }

    // 비밀번호 재설정 제출
    const resetPasswordForm = document.getElementById('findPwdForm');
    if (resetPasswordForm) {
        resetPasswordForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const newPassword = document.getElementById('new-password').value;
            const confirmNewPassword = document.getElementById('confirm-new-password').value;

            if (newPassword.length < 6) {
                alert("새 비밀번호는 6자 이상이어야 합니다.");
                return;
            }
            if (newPassword !== confirmNewPassword) {
                alert("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
                return;
            }

            // 백앤드 통신 필요 (인증번호 전송 API)
            console.log(`[DEV_LOG] Resetting password... (API call to /api/reset-password)`);

            alert("비밀번호가 성공적으로 재설정되었습니다. 로그인 페이지로 이동합니다.");
            window.location.href = '/login';
        });
    }
});