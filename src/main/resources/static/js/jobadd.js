document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('jobAddForm');

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            // 1. 주소 정보 합치기
            const address = document.getElementById('address').value;
            const detail = document.getElementById('detailAddress').value;
            const extra = document.getElementById('extraAddress').value;

            const fullLocation = `${address} ${detail} ${extra}`.trim();
            document.getElementById('location').value = fullLocation;

            if (!address) {
                // [수정] Alert -> Toast (에러)
                showToast("근무지 주소를 검색해주세요.", "error");
                return;
            }

            // 2. 데이터 준비
            const formData = new FormData(form);
            const payload = Object.fromEntries(formData.entries());

            try {
                /* [서버 연동 시 주석 해제]
                   const response = await fetch('/api/job/register', {
                       method: 'POST',
                       headers: { 'Content-Type': 'application/json' },
                       body: JSON.stringify(payload)
                   });

                   if (!response.ok) throw new Error('등록 실패');
                */

                // [임시 성공 처리]
                console.log('등록할 데이터:', payload);

                // Alert -> Toast (성공)
                showToast("일자리가 성공적으로 등록되었습니다!", "success");

                // 목록 페이지 이동 (토스트 메시지를 볼 수 있게 약간의 지연 후 이동)
                setTimeout(() => {
                    window.location.href = '/jobmanage';
                }, 1500);

            } catch (error) {
                console.error('Error:', error);
                // [수정] Alert -> Toast (에러)
                showToast("일자리 등록 중 오류가 발생했습니다.", "error");
            }
        });
    }

    // 모달 배경 클릭 시 닫기
    const overlay = document.getElementById('postcodeOverlay');
    if (overlay) {
        overlay.addEventListener('click', function(e) {
            if (e.target === this) {
                this.style.display = 'none';
            }
        });
    }
});

/* --- Daum 우편번호 서비스 연동 --- */
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