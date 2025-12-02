// 1. 데이터 변수
let jobData = [];
let currentEditId = null;
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', () => {
    loadJobData();

    // 검색 기능
    document.getElementById('searchInput').addEventListener('keyup', function () {
        renderJobList(this.value);
    });
});

// 1-1. DB에서 데이터 로드
async function loadJobData() {
    console.log('[DEBUG] loadJobData 시작');
    try {
        console.log('[DEBUG] API 호출: /api/jobs');
        const response = await fetch('/api/jobs');
        console.log('[DEBUG] 응답 상태:', response.status);
        
        const data = await response.json();
        console.log('[DEBUG] 응답 데이터:', data);
        
        if (data.jobs) {
            // DB 데이터를 화면 표시용 형식으로 변환
            jobData = data.jobs.map(job => ({
                id: job.jobNo,
                date: job.createdAt ? job.createdAt.split('T')[0] : '-',
                region: job.jobWorkLocation || '-',
                title: job.jobTitle || '-',
                salary: job.jobSalary || '-',
                company: job.jobCompanyName || '-',
                contact: '-'  // DB에 연락처 컬럼 없음
            }));
            
            console.log('[SUCCESS] 일자리 데이터 로드 완료:', jobData.length + '개');
            console.log('[DEBUG] 첫 번째 데이터:', jobData[0]);
            renderJobList();
        } else {
            console.error('[ERROR] data.jobs가 없습니다:', data);
            alert('데이터 형식이 올바르지 않습니다.');
        }
    } catch (error) {
        console.error('[ERROR] 일자리 데이터 로드 실패:', error);
        alert('데이터를 불러오는데 실패했습니다: ' + error.message);
    }
}

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
async function submitEdit() {
    if (!currentEditId) return;

    const updatedTitle = document.getElementById('editWork').value.trim();
    const updatedRegion = document.getElementById('editRegion').value.trim();
    const updatedSalary = document.getElementById('editSalary').value.trim();
    const updatedCompany = document.getElementById('editCompany').value.trim();

    if (!updatedTitle || !updatedRegion || !updatedSalary || !updatedCompany) {
        alert('모든 필드를 입력해주세요.');
        return;
    }

    try {
        // API 호출: 수정
        const response = await fetch(`/api/jobs/${currentEditId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                jobTitle: updatedTitle,
                jobWorkLocation: updatedRegion,
                jobSalary: updatedSalary,
                jobCompanyName: updatedCompany
            })
        });

        const result = await response.json();
        
        if (result.success) {
            const jobIndex = jobData.findIndex(j => j.id === currentEditId);
            if (jobIndex > -1) {
                // 로컬 데이터도 업데이트
                jobData[jobIndex] = {
                    ...jobData[jobIndex],
                    title: updatedTitle,
                    region: updatedRegion,
                    salary: updatedSalary,
                    company: updatedCompany
                };
            }

            renderJobList(document.getElementById('searchInput').value);
            showToast('수정되었습니다.', 'success');
            closeEditModal();
        } else {
            alert(result.message || '수정에 실패했습니다.');
        }
    } catch (error) {
        console.error('수정 실패:', error);
        alert('수정 중 오류가 발생했습니다.');
    }
}

// 5. 삭제 기능 (모달 호출)
function deleteJob(id) {
    deleteTargetId = id;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

async function confirmDelete() {
    if (!deleteTargetId) return;

    try {
        // API 호출: 삭제
        const response = await fetch(`/api/jobs/${deleteTargetId}`, {
            method: 'DELETE'
        });

        const result = await response.json();
        
        if (result.success) {
            // 로컬 데이터에서도 제거
            jobData = jobData.filter(j => j.id !== deleteTargetId);
            renderJobList(document.getElementById('searchInput').value);
            showToast('삭제되었습니다.', 'info');
            closeDeleteModal();
        } else {
            alert(result.message || '삭제에 실패했습니다.');
        }
    } catch (error) {
        console.error('삭제 실패:', error);
        alert('삭제 중 오류가 발생했습니다.');
    }
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