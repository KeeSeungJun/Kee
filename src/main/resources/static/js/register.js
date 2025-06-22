// <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
// <script src="https://ssl.daumcdn.net/dmaps/map_js_init/postcode.v2.js"></script>

const modal = document.getElementById('disease-modal');
const openBtn = document.getElementById('open-disease-modal');
const confirmBtn = document.getElementById('confirmBtn');

if(openBtn){
    openBtn.addEventListener('click', () => {
        restoreModalSelections();
        modal.style.display = 'flex';
    });
}

function restoreModalSelections() {
    const hiddenInput = document.getElementById('selectedDiseases');
    const selectedText = hiddenInput.value;
    const selectedArray = selectedText ? selectedText.split(',').map(s => s.trim()) : [];

    const buttons = document.querySelectorAll('.health-buttons button');
    buttons.forEach(btn => btn.classList.remove('selected'));

    buttons.forEach(btn => {
        if(selectedArray.includes(btn.textContent.trim())) {
            btn.classList.add('selected');
        }
    });

    const customInput = document.getElementById('customDisease');
    const customValue = selectedArray.find(val =>
        !Array.from(buttons).some(btn => btn.textContent.trim() === val)
    ) || '';
    customInput.value = customValue;
}

function selectInterest(btn) {
    btn.classList.toggle('selected');
}

if(confirmBtn){
    confirmBtn.addEventListener('click', () => {
        const selectedBtns = document.querySelectorAll('.health-buttons button.selected');
        const selectedValues = Array.from(selectedBtns).map(btn => btn.textContent.trim());

        const customDisease = document.getElementById('customDisease').value.trim();
        if (customDisease) {
            selectedValues.push(customDisease);
        }

        document.getElementById('selectedDiseases').value = selectedValues.join(', ');

        console.log('선택된 질병:', selectedValues);

        modal.style.display = 'none';

    });
}

const modals = document.querySelectorAll('.modal');
modals.forEach(modal => {
    modal.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});

// 모달 끝

let selectedInterests = [];

function selectGender(gender) {
    document.querySelectorAll('.gender-buttons button').forEach(function(button) {
        button.classList.remove('selected');
    });

    const selectedButton = document.getElementById(gender);
    selectedButton.classList.add('selected');
    document.getElementById('gender').value = gender;
}

function toggleAllCheckboxes() {
    const agreeAll = document.getElementById('agree-all').checked;
    document.querySelectorAll('.agree-checkbox').forEach(checkbox => {
        checkbox.checked = agreeAll;
    });
}

function selectInterest(button) {
    const interest = button.textContent;
    if (selectedInterests.includes(interest)) {
        selectedInterests = selectedInterests.filter(i => i !== interest);
        button.classList.remove('selected');
    } else {
        selectedInterests.push(interest);
        button.classList.add('selected');
    }
    document.getElementById('interests').value = selectedInterests.join(',');
}

document.addEventListener('DOMContentLoaded', function() {
    const scrollBoxes = document.querySelectorAll('.scroll-box');
    scrollBoxes.forEach(function(box) {
        const boxHeight = box.scrollHeight;
        const boxMaxHeight = box.clientHeight;
        if (boxHeight > boxMaxHeight) {
            box.classList.add('scroll-active');
        }
    });
});

function toggleDiseaseButtons() {
    const buttonContainer = document.getElementById('interest-buttons');
    const currentDisplay = buttonContainer.style.display;
    buttonContainer.style.display = currentDisplay === 'none' || currentDisplay === '' ? 'block' : 'none';
}

function validateAndSubmit() {
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const confirmPassword = document.getElementById("confirm-password").value.trim();
    const gender = document.getElementById("gender").value.trim();
    const occupation = document.getElementById("occupation").value.trim();
    const selectedDiseases = document.getElementById('selectedDiseases').value.trim();
    const birthdate = document.getElementById("birthdate").value.trim();
    const phone = document.getElementById("phone").value.trim();
    const postcode = document.getElementById("postcode").value.trim();
    const address = document.getElementById("address").value.trim();
    const detailAddress = document.getElementById("detailAddress").value.trim();
    const extraAddress = document.getElementById("extraAddress").value.trim();
    const customDisease = document.getElementById("customDisease").value.trim();
    const agreeAge = document.getElementById("agree-age").checked;
    const agreeTerms = document.getElementById("agree-terms").checked;
    const agreePrivacy = document.getElementById("agree-privacy").checked;

    if (name.length < 2) {
        alert("이름은 두 글자 이상 입력해주세요.");
        return;
    }

    // if (!birthdate || !phone || !verificationCode || !postcode || !address || !detailAddress) {
    //     alert("모든 필수 정보를 입력해주세요.");
    //     return;
    // }

    if (!birthdate || !phone || !address || !detailAddress) {
        alert("모든 필수 정보를 입력해주세요.");
        return;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
        alert("올바른 이메일 형식을 입력해주세요.");
        return;
    }

    if (!password || !confirmPassword) {
        alert("비밀번호를 입력해주세요.");
        return;
    }

    if (password.length < 6) {
        alert("비밀번호는 6자리 이상 입력해주세요!");
        return;
    }

    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }

    if (!selectedDiseases) {
        alert("질병을 선택해 주세요.");
        return;
    }

    if (!occupation) {
        alert("직업을 입력해주세요.");
        return;
    }

    if (!gender) {
        alert("성별을 선택해주세요.");
        return;
    }

    if (!agreeAge || !agreeTerms || !agreePrivacy) {
        alert("모든 필수 약관에 동의하셔야 합니다.");
        return;
    }

    // 조건 통과 → 페이지 이동
    // document.getElementById('registerForm').submit();

    //alert("selectedDiseases" + selectedDiseases);
    //console.log("selectedDiseases : ", selectedDiseases);

    const formData = {
        name: name,
        email: email,
        password: password,
        confirm_password: confirmPassword,
        gender: gender,
        occupation: occupation,
        userHealth: selectedDiseases,
        birthdate: birthdate,
        phone: phone,
        postcode: postcode,
        address: address,
        detailAddress: detailAddress,
        extraAddress: extraAddress,
        customDisease: customDisease,
    };


    //console.log("서버로 보낼 JSON : ", formData);

    fetch('/api/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            console.log('Data:', data)
            if (data.code === 200) {
                console.log('Success:', data.message)
                window.location.href = "/main?user_id=" + data.user_id;
            } else {
                alert(`[ ERROR ]: ${data.message}`)
            }
        })
        .catch(error => {
            console.log('Error:', error)
            alert(`[ ERROR ]: ${error}`)
        });
}

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

function handlePhoneInput() {
    const phoneInput = document.getElementById('phone');
    phoneInput.value = phoneInput.value.replace(/[^0-9]/g, '').slice(0, 11);

    const sendBtn = document.getElementById('send-code-btn');
    sendBtn.disabled = phoneInput.value.length !== 11;
}

// let timerInterval;
// let timeLeft = 180;
//
// function startVerification() {
//     const verifyBtn = document.getElementById('verify-code-btn');
//     verifyBtn.disabled = true;
//     timeLeft = 180;
//     updateTimer();
//
//     clearInterval(timerInterval);
//     timerInterval = setInterval(() => {
//         timeLeft--;
//         updateTimer();
//         if (timeLeft <= 0) {
//             clearInterval(timerInterval);
//             document.getElementById('timer').textContent = '시간 종료';
//             verifyBtn.disabled = true;
//         }
//     }, 1000);
// }
//
// function updateTimer() {
//     const minutes = String(Math.floor(timeLeft / 60)).padStart(2, '0');
//     const seconds = String(timeLeft % 60).padStart(2, '0');
//     document.getElementById('timer').textContent = `${minutes}:${seconds}`;
// }
//
// function handleVerificationInput() {
//     const codeInput = document.getElementById('verification-code');
//     codeInput.value = codeInput.value.replace(/[^0-9]/g, '').slice(0, 6);
//
//     const verifyBtn = document.getElementById('verify-code-btn');
//     verifyBtn.disabled = codeInput.value.length !== 6;
// }
//
// function verifyCode() {
//     alert('인증번호 확인 처리 (실제 로직 필요)');
// }

// 주소
function openPostcodeModal() {
    new daum.Postcode({
        popup: false,
        width: 350,    // 모달 width와 맞춰줘야 함
        height: 400,   // 모달 height와 맞춰줘야 함
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

            document.getElementById('postcodeOverlay').style.display = 'none';
        },
        onclose: function() {
            document.getElementById('postcodeOverlay').style.display = 'none';
        }
    }).embed(document.getElementById('postcodeModal'));

    document.getElementById('postcodeOverlay').style.display = 'flex';
}
document.getElementById('postcodeOverlay').addEventListener('click', function(event) {
    if(event.target === this) {
        this.style.display = 'none';
    }
});