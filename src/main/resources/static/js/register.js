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

document.addEventListener('DOMContentLoaded', () => {
    const phoneInput = document.getElementById('phone');
    if(phoneInput) {
        phoneInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '').slice(0, 11);
        });
    }

    const jobModal = document.getElementById('jobModal');
    if(jobModal) {
        jobModal.addEventListener('click', function(e) {
            if(e.target === this) closeJobModal();
        });
    }

    // 주소 검색 모달 배경 클릭 시 닫기
    const overlay = document.getElementById('postcodeOverlay');
    if (overlay) {
        overlay.addEventListener('click', function(e) {
            if (e.target === this) {
                this.style.display = 'none';
            }
        });
    }
});

function setGender(val, btn) {
    document.querySelectorAll('.gender-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById('gender').value = val;
}

function toggleHealth(btn) {
    btn.classList.toggle('selected');
}

// 비밀번호 토글 기능
function togglePassword(inputId, icon) {
    const input = document.getElementById(inputId);
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

// 주소 검색 모달 열기
function openPostcode() {
    const overlay = document.getElementById('postcodeOverlay');
    const modal = document.getElementById('postcodeModal');

    // 모달을 먼저 표시하여 영역 확보
    overlay.style.display = 'flex';

    // 내용 초기화
    modal.innerHTML = '';

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

            // 검색 완료 후 모달 닫기
            overlay.style.display = 'none';
        },
        width: '100%',
        height: '100%'
    }).embed(modal);
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
    const occupation = document.getElementById('occupation').value.trim();

    if (!email || !pw || !name || !birthdate || !phone || !postcode || !occupation) {
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

function renderJobDepth1() {
    const list = document.getElementById('job-depth-1');
    list.innerHTML = '';
    document.getElementById('job-depth-2').innerHTML = '';
    document.getElementById('job-depth-3').innerHTML = '';

    for (const key in jobData) {
        const li = document.createElement('li');
        li.innerText = key;
        li.onclick = function() {
            Array.from(list.children).forEach(c => c.classList.remove('active'));
            this.classList.add('active');
            renderJobDepth2(key);
        };
        list.appendChild(li);
    }
}

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
    document.getElementById('occupation').value = jobName;
    closeJobModal();
}