let timerInterval;
let remainingTime = 180;

document.addEventListener('DOMContentLoaded', () => {
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 11);
        });
    }

    const codeInput = document.getElementById('auth-code');
    if (codeInput) {
        codeInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 6);
        });
    }
});

function startVerification() {
    const phone = document.getElementById('phone').value.trim();

    if (phone.length !== 11) {
        showToast("휴대전화번호를 11자리로 정확히 입력해주세요.", "error");
        return;
    }

    document.getElementById('verify-area').style.display = 'block';

    clearInterval(timerInterval);
    remainingTime = 180;
    updateTimerDisplay();

    timerInterval = setInterval(() => {
        remainingTime--;
        updateTimerDisplay();
        if (remainingTime <= 0) {
            clearInterval(timerInterval);
            document.getElementById('timer').innerText = "시간초과";
        }
    }, 1000);

    showToast("인증번호가 발송되었습니다. (테스트: 123456)", "info");
}

function updateTimerDisplay() {
    const minutes = String(Math.floor(remainingTime / 60));
    const seconds = String(remainingTime % 60).padStart(2, '0');
    document.getElementById('timer').innerText = `${minutes}:${seconds}`;
}

function verifyCode() {
    const code = document.getElementById('auth-code').value;

    if (remainingTime <= 0) {
        showToast("인증 시간이 만료되었습니다. 재요청해주세요.", "error");
        return;
    }

    if (code === "123456") {
        showToast("인증되었습니다.", "success");
        clearInterval(timerInterval);

        document.getElementById('verify-area').style.display = 'none';
        document.getElementById('phone').disabled = true;
        document.getElementById('nextBtn').disabled = false;
    } else {
        showToast("인증번호가 올바르지 않습니다.", "error");
    }
}

function goToStep2() {
    const userId = document.getElementById('user-id').value;
    const userName = document.getElementById('user-name').value;

    if (!userId || !userName) {
        showToast("아이디와 이름을 입력해주세요.", "error");
        return;
    }

    document.getElementById('step1').style.display = 'none';
    document.getElementById('step2').style.display = 'block';
}

function submitReset() {
    const newPw = document.getElementById('new-password').value;
    const confirmPw = document.getElementById('confirm-new-password').value;

    if (!newPw || !confirmPw) {
        showToast("새 비밀번호를 입력해주세요.", "error");
        return;
    }

    if (newPw.length < 6) {
        showToast("비밀번호는 6자리 이상이어야 합니다.", "error");
        return;
    }

    if (newPw !== confirmPw) {
        showToast("비밀번호가 일치하지 않습니다.", "error");
        return;
    }

    showToast("비밀번호가 성공적으로 변경되었습니다.", "success");
    setTimeout(() => { window.location.href = '/login'; }, 1500);
}