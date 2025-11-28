document.addEventListener('DOMContentLoaded', function() {
    fetchUserProfile();

    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 11);
        });
    }

    const verifyInput = document.getElementById('verification-code');
    if (verifyInput) {
        verifyInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 6);
        });
    }

    const overlay = document.getElementById('postcodeOverlay');
    if (overlay) {
        overlay.addEventListener('click', function(e) {
            if (e.target === this) {
                this.style.display = 'none';
            }
        });
    }
});

/* --- 사용자 정보 조회 --- */
async function fetchUserProfile() {
    try {
        const response = await fetch('/api/user/me', {
            method: 'GET',
            headers: { 'Accept': 'application/json' }
        });

        if (response.ok) {
            const user = await response.json();

            if (user.userName) document.getElementById('name').value = user.userName;
            if (user.birthdate) document.getElementById('birthdate').value = user.birthdate;
            if (user.occupation) document.getElementById('job').value = user.occupation;
            if (user.mobileNumber) document.getElementById('phone').value = user.mobileNumber;
            if (user.customDisease) document.getElementById('customDisease').value = user.customDisease;

            if (user.postcode) document.getElementById('postcode').value = user.postcode;
            if (user.userAddr) document.getElementById('address').value = user.userAddr;
            if (user.detailAddress) document.getElementById('detailAddress').value = user.detailAddress;

            if (user.gender) {
                document.getElementById('gender').value = user.gender;
                const genderBtn = document.querySelector(`.gender-btn[data-value="${user.gender}"]`);
                if (genderBtn) genderBtn.classList.add('active');
            }

            if (user.userHealth) {
                const healthList = user.userHealth.split(',').map(s => s.trim());
                const chips = document.querySelectorAll('.chip-btn');
                chips.forEach(chip => {
                    if (healthList.includes(chip.innerText.trim())) {
                        chip.classList.add('selected');
                    }
                });
            }

        } else {
            console.warn('사용자 정보를 가져오지 못했습니다.');
        }
    } catch (error) {
        console.error('프로필 조회 중 오류 발생:', error);
    }
}

/* --- 주소 검색 --- */
function openPostcodeModal() {
    const overlay = document.getElementById('postcodeOverlay');
    const modal = document.getElementById('postcodeModal');

    new daum.Postcode({
        oncomplete: function(data) {
            var addr = '';
            var extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if(data.userSelectedType === 'R'){
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                document.getElementById('extraAddress').value = extraAddr;
            } else {
                document.getElementById('extraAddress').value = '';
            }

            document.getElementById('postcode').value = data.zonecode;
            document.getElementById('address').value = addr;
            document.getElementById('detailAddress').focus();

            overlay.style.display = 'none';
        },
        width: '100%',
        height: '100%'
    }).embed(modal);

    overlay.style.display = 'flex';
}

function toggleChip(btn) {
    btn.classList.toggle('selected');
}

function selectGender(btn) {
    document.querySelectorAll('.gender-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById('gender').value = btn.getAttribute('data-value');
}

function togglePasswordSection() {
    const content = document.getElementById('pw-content');
    const arrow = document.getElementById('pw-arrow');

    if (content.classList.contains('show')) {
        content.classList.remove('show');
        arrow.classList.remove('fa-chevron-up');
        arrow.classList.add('fa-chevron-down');
    } else {
        content.classList.add('show');
        arrow.classList.remove('fa-chevron-down');
        arrow.classList.add('fa-chevron-up');
    }
}

async function changePassword() {
    const currentPassword = document.getElementById('current-password').value;
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (!currentPassword || !newPassword || !confirmPassword) {
        showToast("모든 비밀번호 입력란을 채워주세요.", "error");
        return;
    }
    if (newPassword.length < 6) {
        showToast("새 비밀번호는 6자리 이상이어야 합니다.", "error");
        return;
    }
    if (newPassword !== confirmPassword) {
        showToast("새 비밀번호가 일치하지 않습니다.", "error");
        return;
    }

    try {
        const response = await fetch('/api/change-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ currentPassword, newPassword })
        });
        const result = await response.json();

        if (response.ok && result.code === 200) {
            showToast('비밀번호가 변경되었습니다. 다시 로그인해주세요.', 'success');
            setTimeout(() => { window.location.href = '/logout'; }, 1500);
        } else {
            showToast(result.message || '비밀번호 변경 실패', 'error');
        }
    } catch (e) {
        console.error(e);
        showToast('서버 오류가 발생했습니다.', 'error');
    }
}

let timerInterval;
let remainingTime = 180;

function startVerification() {
    const phone = document.getElementById('phone').value.trim();
    if (phone.length !== 11) {
        showToast("전화번호를 11자리로 정확히 입력해주세요.", "error");
        return;
    }

    document.getElementById('verification-area').style.display = 'block';

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

    showToast("인증번호가 발송되었습니다.", "info");
}

function updateTimerDisplay() {
    const minutes = String(Math.floor(remainingTime / 60)).padStart(2, '0');
    const seconds = String(remainingTime % 60).padStart(2, '0');
    document.getElementById('timer').textContent = `${minutes}:${seconds}`;
}

function verifyCode() {
    const code = document.getElementById('verification-code').value;
    if (code === "123456") {
        showToast("인증되었습니다!", "success");
        clearInterval(timerInterval);
        document.getElementById('verification-area').style.display = 'none';
        document.getElementById('phone').disabled = true;
    } else {
        showToast("인증번호가 올바르지 않습니다.", "error");
    }
}

function submitProfile() {
    const gender = document.getElementById('gender').value;
    const job = document.getElementById('job').value;
    const birthdate = document.getElementById('birthdate').value;
    const customDisease = document.getElementById('customDisease').value;

    const postcode = document.getElementById('postcode').value;
    const address = document.getElementById('address').value;
    const detailAddress = document.getElementById('detailAddress').value;

    const selectedChips = Array.from(document.querySelectorAll('.chip-btn.selected'))
        .map(btn => btn.innerText);
    const userHealth = selectedChips.join(', ');

    const payload = {
        gender: gender,
        occupation: job,
        birthdate: birthdate,
        userHealth: userHealth,
        customDisease: customDisease,
        postcode: postcode,
        userAddr: address,
        detailAddress: detailAddress
    };

    console.log("전송 데이터:", payload);
    showToast("회원 정보가 수정되었습니다.", "success");

    setTimeout(() => {
        window.location.href = "/profile";
    }, 1500);
}