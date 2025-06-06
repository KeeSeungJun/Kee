function toggleVisibility(id) {
    const input = document.getElementById(id);
    const icon = document.getElementById(`toggle-icon-${id}`);
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

document.getElementById('change-password-btn').addEventListener('click', async () => {
    const currentPassword = document.getElementById('current-password').value;
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    // 1) 클라이언트 측 간단한 유효성 검사
    if (!currentPassword || !newPassword || !confirmPassword) {
        alert("모든 비밀번호 입력란을 채워주세요.");
        return;
    }
    if (newPassword !== confirmPassword) {
        alert("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        return;
    }

    // 2) 서버에 비밀번호 변경 요청
    try {
        const response = await fetch('/api/change-password', {  // 실제 API 주소로 변경 필요
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                currentPassword,
                newPassword
            })
        });

        const result = await response.json();

        // 3) 서버 응답 처리
        if (response.ok) {
            alert('비밀번호가 성공적으로 변경되었습니다.');
            // 필요하면 입력폼 초기화 등 추가 작업
        } else {
            // 서버에서 온 에러 메시지 출력
            alert(result.message || '비밀번호 변경에 실패했습니다.');
        }
    } catch (error) {
        alert('서버와 통신 중 오류가 발생했습니다.');
        console.error(error);
    }
});

function openModal() {
    document.getElementById("diseaseModal").style.display = "block";
}

function toggleDisease(button) {
    button.classList.toggle("selected");
}

// 선택된 질병 저장
function saveDiseases() {
    const selectedButtons = document.querySelectorAll(".disease-option.selected");
    const selectedDiseases = Array.from(selectedButtons).map(btn => btn.textContent);

    // 저장 처리 (예: 콘솔 출력)
    console.log("선택된 질병:", selectedDiseases);

    // 이후 실제 저장 로직을 여기에 작성 가능 (예: 서버 전송)@@@@@@@

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

    if (phone.length !== 11) {
        alert("전화번호를 11자리로 정확히 입력해주세요.");
        return;
    }

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