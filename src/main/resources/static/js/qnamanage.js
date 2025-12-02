// QnA 데이터
let qnaData = [];

let currentStatusFilter = 'all';
let currentQnaId = null;
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', () => {
    loadQnaData();
});

// 데이터 로드
async function loadQnaData() {
    try {
        const keyword = document.getElementById('qnaSearch').value;
        let url = '/api/qnas';
        
        const params = new URLSearchParams();
        if (currentStatusFilter !== 'all') {
            params.append('status', currentStatusFilter);
        }
        if (keyword) {
            params.append('keyword', keyword);
        }
        
        if (params.toString()) {
            url += '?' + params.toString();
        }
        
        const response = await fetch(url);
        const data = await response.json();
        
        qnaData = data.qnas.map(qna => ({
            id: qna.qnaNo,
            date: qna.createdAt ? qna.createdAt.substring(0, 10) : '',
            user: qna.usrId,
            question: qna.qnaContent,
            answer: qna.qnaAnswer || '',
            status: qna.qnaStatus === 'WAITING' ? 'waiting' : 'answered'
        }));
        
        renderQnaList();
    } catch (error) {
        console.error('QnA 데이터 로드 실패:', error);
        showToast('데이터를 불러오는데 실패했습니다.', 'error');
    }
}


// 2. 리스트 렌더링
function renderQnaList() {
    const container = document.getElementById('qnaListContainer');
    container.innerHTML = '';

    if (qnaData.length === 0) {
        container.innerHTML = '<div style="padding:40px; text-align:center; color:#888;">데이터가 없습니다.</div>';
        return;
    }

    qnaData.forEach(qna => {
        const div = document.createElement('div');
        div.className = 'qna-item';

        const statusHtml = qna.status === 'waiting'
            ? '<span class="status-waiting">답변대기</span>'
            : '<span class="status-answered">답변완료</span>';

        div.innerHTML = `
            <div class="col date">${qna.date}</div>
            <div class="col user">${qna.user}</div>
            <div class="col question">${qna.question}</div>
            <div class="col status">${statusHtml}</div>
            <div class="col actions">
                <button class="action-icon-btn" onclick="openModal(${qna.id})" title="답변하기">
                    <i class="fa-solid fa-pen-to-square"></i>
                </button>
                <button class="action-icon-btn delete" onclick="deleteQna(${qna.id})" title="삭제">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </div>
        `;
        container.appendChild(div);
    });
}

function filterStatus(status, btn) {
    currentStatusFilter = status;
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    loadQnaData();
}

function filterQna() {
    loadQnaData();
}

// 3. 모달 열기
function openModal(id) {
    const qna = qnaData.find(q => q.id === id);
    if (!qna) return;

    currentQnaId = id;

    document.getElementById('modalUser').innerText = qna.user;
    document.getElementById('modalDate').innerText = qna.date;
    document.getElementById('modalQuestion').innerText = qna.question;
    document.getElementById('modalAnswer').value = qna.answer;

    document.getElementById('qnaModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('qnaModal').style.display = 'none';
    currentQnaId = null;
}

// 4. 답변 저장 (확인 모달)
function preSubmitAnswer() {
    const answer = document.getElementById('modalAnswer').value.trim();
    if (!answer) {
        showToast("답변 내용을 입력해주세요.", "error");
        return;
    }
    document.getElementById('registerConfirmModal').style.display = 'flex';
}

function closeRegisterConfirmModal() {
    document.getElementById('registerConfirmModal').style.display = 'none';
}

async function confirmSubmitAnswer() {
    const answer = document.getElementById('modalAnswer').value.trim();

    try {
        const response = await fetch(`/api/qnas/${currentQnaId}/answer`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                answer: answer,
                answerBy: 'admin'
            })
        });

        const result = await response.json();
        
        if (result.success) {
            showToast("답변이 등록되었습니다.", "success");
            await loadQnaData();
            closeRegisterConfirmModal();
            closeModal();
        } else {
            showToast(result.message || "답변 등록에 실패했습니다.", "error");
        }
    } catch (error) {
        console.error('답변 등록 실패:', error);
        showToast('답변 등록 중 오류가 발생했습니다.', 'error');
    }
}

// 5. 삭제 기능
function deleteQna(id) {
    deleteTargetId = id;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

async function confirmDelete() {
    if (!deleteTargetId) return;

    try {
        const response = await fetch(`/api/qnas/${deleteTargetId}`, {
            method: 'DELETE'
        });

        const result = await response.json();
        
        if (result.success) {
            showToast("삭제되었습니다.", "info");
            await loadQnaData();
            closeDeleteModal();
        } else {
            showToast(result.message || "삭제에 실패했습니다.", "error");
        }
    } catch (error) {
        console.error('QnA 삭제 실패:', error);
        showToast('삭제 중 오류가 발생했습니다.', 'error');
    }
}

function closeDeleteModal() {
    document.getElementById('deleteConfirmModal').style.display = 'none';
    deleteTargetId = null;
}

function deleteQnaFromModal() {
    if (currentQnaId) {
        const idToDelete = currentQnaId;
        closeModal();
        deleteQna(idToDelete);
    }
}