// 예시 데이터 시작. 주석 처리 하셔야 합니다.
const jobData = [
    { id: 1, 등록일: "2025-05-01", 지역: "서울", 업무: "웹개발자", 월급: "300만원", 업체명: "ABC", 연락처: "010-1234-5678" },
    { id: 2, 등록일: "2025-05-10", 지역: "부산", 업무: "디자이너", 월급: "280만원", 업체명: "XYZ", 연락처: "010-9876-5432" },
    { id: 3, 등록일: "2025-05-12", 지역: "대전", 업무: "마케터", 월급: "250만원", 업체명: "MKT", 연락처: "010-5555-1234" }
];
// 예시 데이터 끝. 주석 처리 하셔야 합니다.

const container = document.querySelector('.job-allinonelist');
let currentEditId = null;
const editModal = document.getElementById('editModal');
const editForm = document.getElementById('editForm');

function truncateText(text, maxLength) {
    return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
}

function renderJobList() {
    container.innerHTML = '';

    jobData.forEach(job => {
        const jobItem = document.createElement('div');
        jobItem.className = 'job-item';
        jobItem.dataset.id = job.id;

        const dateDiv = document.createElement('div');
        dateDiv.className = 'job-date';
        dateDiv.textContent = job.등록일;

        const regionDiv = document.createElement('div');
        regionDiv.className = 'job-region';
        regionDiv.textContent = truncateText(job.지역, 5);

        const workDiv = document.createElement('div');
        workDiv.className = 'job-work';
        workDiv.textContent = truncateText(job.업무, 5);

        jobItem.append(dateDiv, regionDiv, workDiv);
        container.appendChild(jobItem);

        jobItem.addEventListener('click', () => {
            currentEditId = job.id;

            document.getElementById('editWork').value = job.업무;
            document.getElementById('editRegion').value = job.지역;
            document.getElementById('editSalary').value = job.월급;
            document.getElementById('editCompany').value = job.업체명;
            document.getElementById('editContact').value = job.연락처;

            editModal.style.display = 'flex';
        });
    });
}

renderJobList();

editForm.onsubmit = function (e) {
    e.preventDefault();

    const updatedWork = document.getElementById('editWork').value;
    const updatedRegion = document.getElementById('editRegion').value;
    const updatedSalary = document.getElementById('editSalary').value;
    const updatedCompany = document.getElementById('editCompany').value;
    const updatedContact = document.getElementById('editContact').value;

    const job = jobData.find(j => j.id === currentEditId);
    if (job) {
        job.업무 = updatedWork;
        job.지역 = updatedRegion;
        job.월급 = updatedSalary;
        job.업체명 = updatedCompany;
        job.연락처 = updatedContact;
    }

    renderJobList();
    alert('수정 완료!');
    closeEditModal();
};

document.getElementById('deleteInEditModal').onclick = function () {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    const idx = jobData.findIndex(j => j.id === currentEditId);
    if (idx > -1) jobData.splice(idx, 1);

    renderJobList();
    alert('삭제 완료!');
    closeEditModal();
};

document.getElementById('editCancelBtn').onclick = closeEditModal;

function closeEditModal() {
    editModal.style.display = 'none';
}

const modals = document.querySelectorAll('.modal');
modals.forEach(modal => {
    modal.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});