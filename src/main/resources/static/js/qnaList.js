// 초기 데이터 (테스트용)
const qnaList = [
    { id: 1, userId: 'b67890', question: '예약 관련 문의', answer: '안녕하세요. 예약은 홈페이지에서 가능합니다.' },
];

// 현재 로그인한 사용자 ID (테스트를 위해 고정값 사용)
// 실제 환경에서는 서버에서 받아오거나 쿠키에서 읽어야 함
const CURRENT_USER_ID = 'user1234';

function maskId(id) {
    if (!id) return '';
    if (id.length <= 3) return id + '***';
    return id.slice(0, 3) + '***';
}

function shortText(text, limit = 12) {
    return text.length > limit ? text.slice(0, limit) + '...' : text;
}

const container = document.getElementById('qna-list-container');

// ============================================================
// 리스트 렌더링 함수
// ============================================================
function renderQnaList() {
    container.innerHTML = '';

    const unansweredQna = qnaList.filter(qna => !qna.answer);
    const answeredQna = qnaList.filter(qna => qna.answer);

    unansweredQna.forEach(qna => createQnaItem(qna, false));
    answeredQna.forEach(qna => createQnaItem(qna, true));
}

function createQnaItem(qna, isAnswered) {
    const item = document.createElement('div');

    const bgColor = isAnswered ? '#f0f8ff' : '#fff0f0';
    const borderColor = isAnswered ? '#4fb0ff' : '#e74c3c';

    item.style.cssText = `
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: ${bgColor}; 
        border-left: 5px solid ${borderColor}; 
        width: 100%;
        padding: 15px 20px;
        border-radius: 10px;
        margin-bottom: 15px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        cursor: pointer;
        box-sizing: border-box;
    `;

    const questionText = document.createElement('span');
    questionText.innerText = shortText(qna.question);
    questionText.style.fontWeight = 'bold';
    questionText.style.flexGrow = '1';

    const userIdText = document.createElement('span');
    userIdText.innerText = maskId(qna.userId);
    userIdText.style.fontWeight = 'bold';
    userIdText.style.minWidth = '60px';
    userIdText.style.textAlign = 'right';

    item.appendChild(questionText);
    item.appendChild(userIdText);
    item.onclick = () => openModal(qna.id);
    container.appendChild(item);
}

// ============================================================
// 상세 모달 관련 함수 (버튼 배치 로직 수정됨)
// ============================================================
function openModal(id) {
    const qna = qnaList.find(q => q.id === id);
    if (!qna) return;

    const userIdEl = document.getElementById('modal-user-id');
    const questionEl = document.getElementById('modal-question');
    const answerBox = document.getElementById('modal-answer');

    userIdEl.innerText = qna.userId;

    questionEl.innerHTML = '';
    questionEl.innerText = qna.question;

    if (qna.answer && qna.answer.trim()) {
        answerBox.textContent = qna.answer;
        answerBox.style.color = '#333';
    } else {
        answerBox.textContent = '아직 답변이 없습니다.';
        answerBox.style.color = '#888';
    }

    const footerBtnGroup = document.querySelector('#qna-modal .modal-button-group');
    footerBtnGroup.innerHTML = ''; // 초기화

    footerBtnGroup.style.display = 'block';

    const isMyPost = (qna.userId === CURRENT_USER_ID || qna.userId === 'b67890');
    const hasAnswer = !!qna.answer;

    if (isMyPost && !hasAnswer) {
        const topRow = document.createElement('div');
        topRow.style.display = 'flex';
        topRow.style.gap = '10px';
        topRow.style.marginBottom = '10px';

        const editBtn = createButton('수정', '#4fb0ff', () => enableEditMode(id));
        const delBtn = createButton('삭제', '#ff4d4d', () => deleteInquiry(id));

        topRow.appendChild(editBtn);
        topRow.appendChild(delBtn);
        footerBtnGroup.appendChild(topRow);

        const closeBtn = createButton('닫기', '#e0e0e0', closeModal);
        closeBtn.style.color = '#333';
        closeBtn.style.width = '100%';
        footerBtnGroup.appendChild(closeBtn);

    } else if (isMyPost && hasAnswer) {

        const row = document.createElement('div');
        row.style.display = 'flex';
        row.style.gap = '10px';

        const delBtn = createButton('삭제', '#ff4d4d', () => deleteInquiry(id));
        const closeBtn = createButton('닫기', '#e0e0e0', closeModal);
        closeBtn.style.color = '#333';

        row.appendChild(delBtn);
        row.appendChild(closeBtn);
        footerBtnGroup.appendChild(row);

    } else {

        const closeBtn = createButton('닫기', '#e0e0e0', closeModal);
        closeBtn.style.color = '#333';
        closeBtn.style.width = '100%';
        footerBtnGroup.appendChild(closeBtn);
    }

    document.getElementById('qna-modal').style.display = 'block';
    document.getElementById('modal-overlay').style.display = 'block';
}

function createButton(text, bgColor, onClickFunc) {
    const btn = document.createElement('button');
    btn.innerText = text;
    btn.className = 'secondary-modal-btn';
    btn.style.backgroundColor = bgColor;
    btn.style.color = 'white';
    btn.style.flex = '1';
    btn.style.border = 'none';
    btn.style.borderRadius = '5px';
    btn.style.padding = '12px 0';
    btn.style.cursor = 'pointer';
    btn.style.fontWeight = 'bold';
    btn.style.fontSize = '16px';
    btn.onclick = onClickFunc;
    return btn;
}

function closeModal() {
    document.getElementById('qna-modal').style.display = 'none';
    document.getElementById('write-modal').style.display = 'none';
    document.getElementById('modal-overlay').style.display = 'none';
    document.getElementById('modal-answer').textContent = '';
}

// ============================================================
// 문의 수정 (Update) 로직
// ============================================================
function enableEditMode(id) {
    const qna = qnaList.find(q => q.id === id);
    const questionEl = document.getElementById('modal-question');

    const textarea = document.createElement('textarea');
    textarea.className = 'write-textarea';
    textarea.style.height = '100px';
    textarea.value = qna.question;
    textarea.id = 'edit-question-input';

    questionEl.innerHTML = '';
    questionEl.appendChild(textarea);

    const footerBtnGroup = document.querySelector('#qna-modal .modal-button-group');
    footerBtnGroup.innerHTML = '';
    footerBtnGroup.style.display = 'flex'; // 다시 한 줄로
    footerBtnGroup.style.gap = '10px';

    const saveBtn = createButton('저장', '#4fb0ff', () => submitEdit(id));
    const cancelBtn = createButton('취소', '#e0e0e0', () => openModal(id));
    cancelBtn.style.color = '#333';

    footerBtnGroup.appendChild(saveBtn);
    footerBtnGroup.appendChild(cancelBtn);
}

function submitEdit(id) {
    const newText = document.getElementById('edit-question-input').value;
    if (!newText.trim()) {
        alert("내용을 입력해주세요.");
        return;
    }

    const qna = qnaList.find(q => q.id === id);
    qna.question = newText;

    alert("수정되었습니다.");
    renderQnaList();
    openModal(id);
}

// ============================================================
// 문의 삭제 (Delete) 로직
// ============================================================
function deleteInquiry(id) {
    if (confirm("정말 삭제하시겠습니까?")) {
        const index = qnaList.findIndex(q => q.id === id);
        if (index > -1) {
            qnaList.splice(index, 1);
            alert("삭제되었습니다.");
            renderQnaList();
            closeModal();
        }
    }
}

// ============================================================
// 문의 작성 (Create) 모달 로직
// ============================================================
function openWriteModal() {
    document.getElementById('write-modal').style.display = 'flex';
    document.getElementById('modal-overlay').style.display = 'block';
    document.getElementById('new-question-text').value = '';
    document.getElementById('new-question-text').focus();
}

function closeWriteModal() {
    document.getElementById('write-modal').style.display = 'none';
    document.getElementById('modal-overlay').style.display = 'none';
}

function submitInquiry() {
    const text = document.getElementById('new-question-text').value.trim();

    if (!text) {
        alert("문의 내용을 입력해주세요.");
        return;
    }

    const newQna = {
        id: Date.now(),
        userId: CURRENT_USER_ID,
        question: text,
        answer: ''
    };

    qnaList.unshift(newQna);
    renderQnaList();

    alert("문의가 등록되었습니다.");
    closeWriteModal();
}

renderQnaList();