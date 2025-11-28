document.addEventListener('DOMContentLoaded', () => {
    // 생년월일 자동 설정
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('birthdate').value = today;

    const form = document.getElementById('registerAdminForm');

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const pw = document.getElementById('password').value;
            const confirmPw = document.getElementById('confirm-password').value;
            const address = document.getElementById('address').value;

            if (pw !== confirmPw) {
                showToast('비밀번호가 일치하지 않습니다.', 'error');
                return;
            }
            if (pw.length < 6) {
                showToast('비밀번호는 6자리 이상이어야 합니다.', 'error');
                return;
            }

            // 사업자 번호 확인
            const businessNum = document.getElementById('businessNum').value;
            if (businessNum.length !== 10) {
                showToast('사업자 등록 번호 10자리를 입력해주세요.', 'error');
                return;
            }

            // 주소 확인
            if (!address) {
                showToast('사업장 주소를 검색해주세요.', 'error');
                return;
            }

            const formData = new FormData(form);
            const payload = Object.fromEntries(formData.entries());

            // [테스트 모드] 서버 통신 없이 성공 처리
            console.log('[TEST] 전송할 데이터:', payload);

            // 성공 모달 띄우기
            document.getElementById('signupSuccessModal').style.display = 'flex';

            /* // --- 실제 서버 통신 코드 (나중에 주석 해제) ---
            try {
                const response = await fetch('/api/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                const result = await response.json();

                if (response.ok && result.code === 200) {
                    // 성공 시 모달 띄우기
                    document.getElementById('signupSuccessModal').style.display = 'flex';
                } else {
                    showToast(`[오류] ${result.message}`, 'error');
                }
            } catch (error) {
                console.error('Register Error:', error);
                showToast('서버 통신 중 오류가 발생했습니다.', 'error');
            }
            */
        });
    }

    // 입력값 숫자만 허용 (연락처, 사업자번호)
    const phoneInput = document.getElementById('phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function () {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 11);
        });
    }

    const bizNumInput = document.getElementById('businessNum');
    if (bizNumInput) {
        bizNumInput.addEventListener('input', function () {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10);
        });
    }

    // 모달 배경 클릭 시 닫기
    const overlay = document.getElementById('postcodeOverlay');
    if (overlay) {
        overlay.addEventListener('click', function (e) {
            if (e.target === this) {
                this.style.display = 'none';
            }
        });
    }
});

// 모달 닫기 및 로그인 페이지 이동
function goToLogin() {
    window.location.href = '/login';
}

/* --- Daum 우편번호 서비스 연동 --- */
function openPostcodeModal() {
    const overlay = document.getElementById('postcodeOverlay');
    const modal = document.getElementById('postcodeModal');

    new daum.Postcode({
        oncomplete: function (data) {
            var addr = '';
            var extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
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