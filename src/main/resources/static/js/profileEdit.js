// src/main/resources/static/js/profileEdit.js

function toggleVisibility(id, iconId) {
    const input = document.getElementById(id);
    const icon = document.getElementById(iconId);
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

function selectGender(button) {
    const buttons = document.querySelectorAll('.gender-btn');
    buttons.forEach(btn => btn.classList.remove('active'));
    button.classList.add('active');
}

// ... (omitted change-password listener) ...

function openModal() {
    // Issue 1 Fix: 모달이 정상적으로 열리도록 수정
    document.getElementById("diseaseModal").style.display = "block";
}

function toggleDisease(button) {
    button.classList.toggle("selected");
}

function saveDiseases() {
    const selectedButtons = document.querySelectorAll(".disease-option.selected");
    const selectedDiseases = Array.from(selectedButtons).map(btn => btn.textContent);

    console.log("선택된 질병:", selectedDiseases);

    document.getElementById("diseaseModal").style.display = "none";
}

window.onclick = function(event) {
    const modal = document.getElementById("diseaseModal");
    if (event.target == modal) {
        modal.style.display = "none";
    }
};

// 인증
let timerInterval;
let remainingTime = 180;

function startVerification() {
    const phone = document.getElementById('phone').value.trim();

    // Issue 2-2 Fix: Timer should only start when the phone number is valid (11 digits).
    if (phone.length !== 11) {
        alert("전화번호를 11자리로 정확히 입력해주세요.");
        return;
    }

    // --- Timer Start Logic ---
    clearInterval(timerInterval);
    remainingTime = 180;
    updateTimerDisplay();

    timerInterval = setInterval(() => {
        remainingTime--;
        updateTimerDisplay();

        if (remainingTime <= 0) {
            clearInterval(timerInterval);
            document.getElementById('timer').textContent = "시간초과";
        }
    }, 1000);
}

function updateTimerDisplay() {
    const minutes = String(Math.floor(remainingTime / 60)).padStart(2, '0');
    const seconds = String(remainingTime % 60).padStart(2, '0');
    document.getElementById('timer').textContent = `${minutes}:${seconds}`;
}

function verifyCode() {
    const code = document.getElementById('verification-code').value;
    if (code.length < 6) {
        alert("인증번호 6자리를 입력해주세요.");
        return;
    }

    alert("인증 완료!");
}

// Issue 2-2 Fix: Input formatting logic moved to DOMContentLoaded
document.addEventListener('DOMContentLoaded', function() {
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            // 번호 입력 시 숫자만 허용하고 11자리로 제한
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 11);
        });
    }

    const verificationInput = document.getElementById('verification-code');
    if (verificationInput) {
        verificationInput.addEventListener('input', function() {
            // 인증번호 입력 시 숫자만 허용하고 6자리로 제한
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 6);
        });
    }
});