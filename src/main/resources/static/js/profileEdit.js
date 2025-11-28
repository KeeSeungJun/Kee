/* 직업 분류 데이터 (확장됨) */
const jobData = {
    "경비·청소·가사": {
        "경비·보안": ["아파트 경비원", "건물 보안요원", "주차 관리원", "학교 배움터 지킴이", "무인경비 시스템 관제원"],
        "청소·미화": ["건물 청소원(오피스/상가)", "아파트 미화원", "도로 환경미화원", "특수 청소원(계단/왁스)", "객실 청소원(룸메이드)"],
        "가사·육아": ["가사 도우미", "육아 도우미(베이비시터)", "산후 조리사", "간병인(가정)"]
    },
    "보건·의료·복지": {
        "사회복지": ["사회복지사", "요양보호사", "장애인 활동보조인", "보육교사", "직업상담사"],
        "의료·간호": ["간호사", "간호조무사", "간병인(병원)", "병원 코디네이터", "원무 행정원"],
        "치료·재활": ["물리치료사", "작업치료사", "재활 트레이너", "임상병리사"]
    },
    "운전·운송·배송": {
        "승객 운송": ["택시 기사", "버스 기사(시내/고속)", "마을버스 기사", "통학버스 운전원", "대리운전"],
        "화물 운송": ["화물차 기사", "트럭 운전원", "지게차 운전원", "특수차량 운전원(레미콘 등)"],
        "배송·택배": ["택배 배송원", "음식 배달원", "퀵서비스", "우편물 집배원"]
    },
    "건설·건축·시설": {
        "시설관리": ["아파트 관리소장", "빌딩 시설관리(전기/기계)", "보일러/공조 냉동 기사", "조경 관리사", "건물 영선원"],
        "건설 현장": ["건설 단순 노무원", "도장공(페인트)", "방수공", "미장공", "목수", "철근공", "전기 공사 기사"],
        "기계·중장비": ["굴착기 운전원", "타워크레인 기사", "불도저 운전원"]
    },
    "요식·서비스": {
        "음식·조리": ["한식 조리사", "중식/일식/양식 조리사", "단체급식 조리사", "주방 보조", "설거지/세척원"],
        "서빙·매장": ["홀 서빙", "매장 관리/카운터", "편의점 스태프", "주유소 주유원"],
        "여행·숙박": ["호텔리어(프런트)", "모텔 관리", "여행 가이드", "문화재 해설사"]
    },
    "경영·사무·금융": {
        "행정·사무": ["일반 사무원", "경리/회계/총무", "비서", "인사/노무 담당자", "데이터 입력원"],
        "금융·보험": ["보험 설계사", "은행원", "증권 중개인", "손해 사정사"]
    },
    "교육·강사": {
        "학교·유치원": ["방과후 교사", "유치원 교사", "급식 도우미", "통학 도우미"],
        "학원·과외": ["학원 강사", "학습지 교사", "예체능 강사(피아노/미술)"],
        "전문 강사": ["숲 해설가", "한자 지도사", "직업 훈련 교사"]
    },
    "생산·제조·농림": {
        "생산·제조": ["제품 조립원", "포장/검수원", "식품 가공원", "섬유/의류 봉제원", "기계 조작원"],
        "농림어업": ["농작물 재배원", "조경 식재원", "양식장 관리원", "임업 종사자"]
    }
};

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

    // 모달 배경 클릭 시 닫기
    const jobModal = document.getElementById('jobModal');
    if(jobModal) {
        jobModal.addEventListener('click', function(e) {
            if(e.target === this) closeJobModal();
        });
    }
});

/* --- 직업 선택 로직 --- */
function toggleJobSearch() {
    const slide = document.getElementById('job-search-area');
    const arrow = document.getElementById('job-arrow');

    if (slide.style.display === 'block') {
        slide.style.display = 'none';
        arrow.classList.remove('fa-chevron-up');
        arrow.classList.add('fa-chevron-down');
    } else {
        slide.style.display = 'block';
        arrow.classList.remove('fa-chevron-down');
        arrow.classList.add('fa-chevron-up');
    }
}

function openJobModal() {
    document.getElementById('jobModal').style.display = 'flex';
    renderJobDepth1();
}

function closeJobModal() {
    document.getElementById('jobModal').style.display = 'none';
}

// 1차 분류 렌더링
function renderJobDepth1() {
    const list = document.getElementById('job-depth-1');
    list.innerHTML = '';
    document.getElementById('job-depth-2').innerHTML = '';
    document.getElementById('job-depth-3').innerHTML = '';

    for (const key in jobData) {
        const li = document.createElement('li');
        li.innerText = key;
        li.onclick = function() {
            // Active 스타일 적용
            Array.from(list.children).forEach(c => c.classList.remove('active'));
            this.classList.add('active');
            renderJobDepth2(key);
        };
        list.appendChild(li);
    }
}

// 2차 분류 렌더링
function renderJobDepth2(depth1Key) {
    const list = document.getElementById('job-depth-2');
    list.innerHTML = '';
    document.getElementById('job-depth-3').innerHTML = '';

    const subData = jobData[depth1Key];
    for (const key in subData) {
        const li = document.createElement('li');
        li.innerText = key;
        li.onclick = function() {
            Array.from(list.children).forEach(c => c.classList.remove('active'));
            this.classList.add('active');
            renderJobDepth3(depth1Key, key);
        };
        list.appendChild(li);
    }
}

// 3차 분류 렌더링 및 선택 완료
function renderJobDepth3(depth1Key, depth2Key) {
    const list = document.getElementById('job-depth-3');
    list.innerHTML = '';

    const jobs = jobData[depth1Key][depth2Key];
    jobs.forEach(job => {
        const li = document.createElement('li');
        li.innerText = job;
        li.onclick = function() {
            selectFinalJob(job);
        };
        list.appendChild(li);
    });
}

function selectFinalJob(jobName) {
    document.getElementById('job').value = jobName;
    closeJobModal();
}

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

            // 직업 정보가 있으면 input에 넣기
            if (user.occupation) {
                document.getElementById('job').value = user.occupation;
            }

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