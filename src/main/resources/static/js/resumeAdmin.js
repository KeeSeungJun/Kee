// Mock Data (지원자 목록)
let applicants = [
    {
        id: 1, date: "2024-05-20", name: "김철수", job: "아파트 경비원",
        age: 68, gender: "남", phone: "010-1111-2222", address: "대전 서구 둔산동",
        health: "고혈압 약 복용중, 거동 문제 없음",
        career: "경비 업무 3년, 청소 업무 1년",
        status: "waiting", comment: ""
    },
    {
        id: 2, date: "2024-05-19", name: "이영희", job: "요양보호사",
        age: 62, gender: "여", phone: "010-3333-4444", address: "대전 유성구 봉명동",
        health: "건강 양호",
        career: "요양보호사 자격증 보유, 실무 5년",
        status: "pass", comment: "5월 25일 면접 예정"
    },
    {
        id: 3, date: "2024-05-18", name: "박민수", job: "주차 관리",
        age: 70, gender: "남", phone: "010-5555-6666", address: "대전 중구 은행동",
        health: "관절염 있음",
        career: "관련 경력 없음",
        status: "fail", comment: "거주지가 근무지와 멉니다."
    }
];

let currentFilter = 'all';
let selectedApplicantId = null;

document.addEventListener('DOMContentLoaded', () => {
    renderList();
});

// 리스트 렌더링
function renderList() {
    const container = document.getElementById('applicantListContainer');
    container.innerHTML = '';

    const filtered = applicants.filter(app => currentFilter === 'all' || app.status === currentFilter);

    if (filtered.length === 0) {
        container.innerHTML = '<div style="text-align:center; padding:40px; color:#888;">데이터가 없습니다.</div>';
        return;
    }

    filtered.forEach(app => {
        const div = document.createElement('div');
        div.className = 'applicant-item';

        let statusBadge = '';
        if (app.status === 'waiting') statusBadge = '<span class="status-badge status-waiting">대기</span>';
        else if (app.status === 'pass') statusBadge = '<span class="status-badge status-pass">합격</span>';
        else if (app.status === 'fail') statusBadge = '<span class="status-badge status-fail">불합격</span>';

        div.innerHTML = `
            <div class="col date">${app.date}</div>
            <div class="col name">${app.name}</div>
            <div class="col job">${app.job}</div>
            <div class="col status">${statusBadge}</div>
            <div class="col actions">
                <button class="action-btn" onclick="openReviewModal(${app.id})">심사하기</button>
            </div>
        `;
        container.appendChild(div);
    });
}

// 필터링
function filterList(status, btn) {
    currentFilter = status;
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    renderList();
}

// 모달 열기
function openReviewModal(id) {
    const app = applicants.find(a => a.id === id);
    if (!app) return;

    selectedApplicantId = id;

    // 정보 채우기
    document.getElementById('modalName').innerText = app.name;
    document.getElementById('modalInfo').innerText = `${app.gender} / ${app.age}세`;
    document.getElementById('modalPhone').innerText = app.phone;
    document.getElementById('modalAddr').innerText = app.address;
    document.getElementById('modalHealth').innerText = app.health;
    document.getElementById('modalCareer').innerText = app.career;
    document.getElementById('adminComment').value = app.comment;

    // 라디오 버튼 상태 설정
    const radios = document.getElementsByName('status');
    radios.forEach(r => {
        if (r.value === app.status) r.checked = true;
    });

    document.getElementById('resumeModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('resumeModal').style.display = 'none';
}

// [수정] 저장 전 확인 모달
function preSubmitReview() {
    if (!selectedApplicantId) return;

    // 결과 선택 여부 확인 (선택사항)
    // ...

    document.getElementById('saveConfirmModal').style.display = 'flex';
}

function closeSaveConfirmModal() {
    document.getElementById('saveConfirmModal').style.display = 'none';
}

// [수정] 실제 저장 로직
function confirmSubmitReview() {
    const radios = document.getElementsByName('status');
    let selectedStatus = 'waiting';
    for (const r of radios) {
        if (r.checked) {
            selectedStatus = r.value;
            break;
        }
    }

    const comment = document.getElementById('adminComment').value;

    // 데이터 업데이트 (Mock)
    const target = applicants.find(a => a.id === selectedApplicantId);
    if (target) {
        target.status = selectedStatus;
        target.comment = comment;

        showToast('심사 결과가 저장되었습니다.', 'success');
        renderList();

        closeSaveConfirmModal();
        closeModal();
    }
}