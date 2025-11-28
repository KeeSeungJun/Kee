// 1. 샘플 하드코딩 데이터
let jobData = [
    {
        id: 1,
        date: "2025-05-01",
        region: "대전 도안동",
        title: "웹개발자 모집",
        salary: "월급 300만원",
        company: "ABC테크",
        contact: "010-1234-5678"
    },
    {
        id: 2,
        date: "2025-05-10",
        region: "대전 태평동",
        title: "시니어 디자이너",
        salary: "월급 280만원",
        company: "XYZ디자인",
        contact: "010-9876-5432"
    },
    {
        id: 3,
        date: "2025-05-12",
        region: "대전 유성구",
        title: "마케터",
        salary: "월급 250만원",
        company: "MKT그룹",
        contact: "010-5555-1234"
    },
    {
        id: 4,
        date: "2025-05-15",
        region: "대전 중구",
        title: "시설 관리직",
        salary: "시급 12,000원",
        company: "행복시설",
        contact: "010-1111-2222"
    }
];

let currentEditId = null;
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', () => {
    renderJobList();

    // 검색 기능
    document.getElementById('searchInput').addEventListener('keyup', function () {
        renderJobList(this.value);
    });
});

// 2. 리스트 렌더링
function renderJobList(keyword = '') {
    const container = document.getElementById('jobListContainer');
    container.innerHTML = '';

    const filteredData = jobData.filter(job => {
        return job.title.includes(keyword) ||
            job.region.includes(keyword) ||
            job.company.includes(keyword);
    });

    if (filteredData.length === 0) {
        container.innerHTML = '<div style="padding:40px; text-align:center; color:#888;">검색 결과가 없습니다.</div>';
        return;
    }

    filteredData.forEach(job => {
        const div = document.createElement('div');
        div.className = 'job-item';

        // PC/Mobile 공통 구조 생성
        div.innerHTML = `
            <div class="col date">${job.date}</div>
            <div class="col info">
                <strong>${job.title}</strong>
                <span>${job.region}</span>
            </div>
            <div class="col salary">${job.salary}</div>
            <div class="col company">${job.company}</div>
            <div class="col actions">
                <button class="action-icon-btn" onclick="openEditModal(${job.id})" title="수정">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="action-icon-btn delete" onclick="deleteJob(${job.id})" title="삭제">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </div>
        `;
        container.appendChild(div);
    });
}

// 3. 수정 모달 열기
function openEditModal(id) {
    const job = jobData.find(j => j.id === id);
    if (!job) return;

    currentEditId = id;

    document.getElementById('editWork').value = job.title;
    document.getElementById('editRegion').value = job.region;
    document.getElementById('editSalary').value = job.salary;
    document.getElementById('editCompany').value = job.company;
    document.getElementById('editContact').value = job.contact;

    document.getElementById('editModal').style.display = 'flex';
}

function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
    currentEditId = null;
}

// 4. 수정 저장
function submitEdit() {
    if (!currentEditId) return;

    const updatedTitle = document.getElementById('editWork').value;
    const updatedRegion = document.getElementById('editRegion').value;
    const updatedSalary = document.getElementById('editSalary').value;
    const updatedCompany = document.getElementById('editCompany').value;
    const updatedContact = document.getElementById('editContact').value;

    const jobIndex = jobData.findIndex(j => j.id === currentEditId);
    if (jobIndex > -1) {
        // 데이터 업데이트
        jobData[jobIndex] = {
            ...jobData[jobIndex],
            title: updatedTitle,
            region: updatedRegion,
            salary: updatedSalary,
            company: updatedCompany,
            contact: updatedContact
        };

        console.log("Updated:", jobData[jobIndex]);

        renderJobList(document.getElementById('searchInput').value);

        // Toast 메시지
        showToast('수정되었습니다.', 'success');

        closeEditModal();
    }
}

// 5. 삭제 기능 (모달 호출)
function deleteJob(id) {
    deleteTargetId = id;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

function confirmDelete() {
    if (deleteTargetId) {
        jobData = jobData.filter(j => j.id !== deleteTargetId);
        renderJobList(document.getElementById('searchInput').value);

        showToast('삭제되었습니다.', 'info');
    }
    closeDeleteModal();
}

function closeDeleteModal() {
    document.getElementById('deleteConfirmModal').style.display = 'none';
    deleteTargetId = null;
}

// 수정 모달 내 삭제 버튼 클릭 시
function deleteJobFromModal() {
    if (currentEditId) {
        const idToDelete = currentEditId; // ID 백업
        closeEditModal(); // 수정 모달 닫기
        deleteJob(idToDelete); // 삭제 확인 모달 열기
    }
}