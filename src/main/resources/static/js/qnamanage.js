// 1. 샘플 하드코딩 데이터
let qnaData = [
    {id: 1, date: "2024-05-20", user: "user1234", question: "비밀번호 변경이 안됩니다.", answer: "", status: "waiting"},
    {
        id: 2,
        date: "2024-05-19",
        user: "hong_gd",
        question: "일자리 지원 후 연락은 언제 오나요?",
        answer: "보통 1주일 이내에 연락 드립니다.",
        status: "answered"
    },
    {
        id: 3,
        date: "2024-05-18",
        user: "senior_01",
        question: "프로필 변경 방법 알려주세요.",
        answer: "프로필 수정 메뉴에서 가능합니다.",
        status: "answered"
    },
    {id: 4, date: "2024-05-15", user: "newbie", question: "앱 설치가 안돼요.", answer: "", status: "waiting"}
];

let currentStatusFilter = 'all';
let currentQnaId = null;
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', () => {
    renderQnaList();
});

// 2. 리스트 렌더링
function renderQnaList() {
    const container = document.getElementById('qnaListContainer');
    const keyword = document.getElementById('qnaSearch').value.toLowerCase();
    container.innerHTML = '';

    const filteredData = qnaData.filter(qna => {
        if (currentStatusFilter !== 'all' && qna.status !== currentStatusFilter) return false;
        return qna.user.toLowerCase().includes(keyword) ||
            qna.question.toLowerCase().includes(keyword);
    });

    if (filteredData.length === 0) {
        container.innerHTML = '<div style="padding:40px; text-align:center; color:#888;">데이터가 없습니다.</div>';
        return;
    }

    // 정렬 (대기중 상단)
    filteredData.sort((a, b) => {
        if (a.status === b.status) return b.id - a.id;
        return a.status === 'waiting' ? -1 : 1;
    });

    filteredData.forEach(qna => {
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
    renderQnaList();
}

function filterQna() {
    renderQnaList();
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

function confirmSubmitAnswer() {
    const answer = document.getElementById('modalAnswer').value.trim();
    const index = qnaData.findIndex(q => q.id === currentQnaId);

    if (index > -1) {
        qnaData[index].answer = answer;
        qnaData[index].status = 'answered';

        showToast("답변이 등록되었습니다.", "success");
        renderQnaList();
        closeRegisterConfirmModal();
        closeModal();
    }
}

// 5. 삭제 기능
function deleteQna(id) {
    deleteTargetId = id;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

function confirmDelete() {
    if (deleteTargetId) {
        qnaData = qnaData.filter(q => q.id !== deleteTargetId);
        renderQnaList();
        showToast("삭제되었습니다.", "info");
    }
    closeDeleteModal();
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