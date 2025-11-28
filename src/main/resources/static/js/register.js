function setGender(val, btn) {
    document.querySelectorAll('.gender-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById('gender').value = val;
}

function toggleHealth(btn) {
    btn.classList.toggle('selected');
}

function openPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;
            var extraAddr = '';

            if(data.userSelectedType === 'R'){
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)) extraAddr += data.bname;
                if(data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if(extraAddr !== '') extraAddr = ' (' + extraAddr + ')';
            }

            document.getElementById('postcode').value = data.zonecode;
            document.getElementById('address').value = addr;
            document.getElementById('extraAddress').value = extraAddr;
            document.getElementById('detailAddress').focus();
        }
    }).open();
}

function submitRegister() {
    const email = document.getElementById('email').value.trim();
    const pw = document.getElementById('password').value;
    const confirmPw = document.getElementById('confirm-password').value;
    const name = document.getElementById('name').value.trim();
    const gender = document.getElementById('gender').value;
    const birthdate = document.getElementById('birthdate').value;
    const phone = document.getElementById('phone').value.trim();
    const postcode = document.getElementById('postcode').value;

    if (!email || !pw || !name || !birthdate || !phone || !postcode) {
        showToast("필수 항목을 모두 입력해주세요.", "error");
        return;
    }

    if (pw !== confirmPw) {
        showToast("비밀번호가 일치하지 않습니다.", "error");
        return;
    }

    if (!gender) {
        showToast("성별을 선택해주세요.", "error");
        return;
    }

    const healthChips = Array.from(document.querySelectorAll('.chip.selected')).map(c => c.innerText);
    document.getElementById('userHealth').value = healthChips.join(', ');

    const form = document.getElementById('registerForm');
    const formData = new FormData(form);
    const payload = Object.fromEntries(formData.entries());

    fetch('/api/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(payload)
    })
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                showToast('회원가입이 완료되었습니다! 로그인 해주세요.', 'success');
                setTimeout(() => { window.location.href = '/login'; }, 1500);
            } else {
                showToast(`[오류] ${data.message || '회원가입에 실패했습니다.'}`, 'error');
            }
        })
        .catch(err => {
            console.error('Register Error:', err);
            showToast('서버 통신 중 오류가 발생했습니다.', 'error');
        });
}

document.addEventListener('DOMContentLoaded', () => {
    const phoneInput = document.getElementById('phone');
    if(phoneInput) {
        phoneInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 11);
        });
    }
});