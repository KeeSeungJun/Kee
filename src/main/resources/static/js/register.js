/* 직업 분류 데이터 (DB에서 로드) */
let jobData = {};
let jobCodeMap = {}; // 직업명 -> 직업코드 매핑

// 페이지 로드 시 직업 분류 데이터 가져오기
async function loadJobCategories() {
    try {
        const response = await fetch('/api/jobs/categories');
        const data = await response.json();
        
        if (data.categories) {
            // JOB_CATEGORY 데이터를 3단계 구조로 변환
            const categories = data.categories;
            jobData = {};
            jobCodeMap = {};
            
            categories.forEach(cat => {
                const majorName = cat.majorName;
                const middleName = cat.middleName;
                const jobName = cat.jobName;
                const jobCode = cat.jobCode;
                
                if (!jobData[majorName]) {
                    jobData[majorName] = {};
                }
                
                if (!jobData[majorName][middleName]) {
                    jobData[majorName][middleName] = [];
                }
                
                // 중복 방지
                if (!jobData[majorName][middleName].includes(jobName)) {
                    jobData[majorName][middleName].push(jobName);
                    // 직업명 -> 직업코드 매핑 저장
                    jobCodeMap[jobName] = jobCode;
                }
            });
            
            console.log(`직업 분류 로드 완료: ${categories.length}개`);
        } else {
            console.error('직업 분류 로드 실패:', data.message);
        }
    } catch (error) {
        console.error('직업 분류 로드 에러:', error);
    }
}

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

document.addEventListener('DOMContentLoaded', () => {
    // 직업 분류 데이터 로드
    loadJobCategories();
    
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
});

/* --- 직업 선택 기능 --- */
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
    
    // 직업 코드 조회
    const jobCode = jobCodeMap[jobName];
    
    // hidden field에 직업 코드도 저장
    let jobCodeInput = document.getElementById('usrHopeJobCode');
    if (!jobCodeInput) {
        jobCodeInput = document.createElement('input');
        jobCodeInput.type = 'hidden';
        jobCodeInput.id = 'usrHopeJobCode';
        jobCodeInput.name = 'usrHopeJobCode';
        document.getElementById('registerForm').appendChild(jobCodeInput);
    }
    jobCodeInput.value = jobCode || '';
    
    let jobNameInput = document.getElementById('usrHopeJobName');
    if (!jobNameInput) {
        jobNameInput = document.createElement('input');
        jobNameInput.type = 'hidden';
        jobNameInput.id = 'usrHopeJobName';
        jobNameInput.name = 'usrHopeJobName';
        document.getElementById('registerForm').appendChild(jobNameInput);
    }
    jobNameInput.value = jobName;
    
    closeJobModal();
}