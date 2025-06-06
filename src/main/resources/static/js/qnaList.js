// 예시 데이터 입니다@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Add commentMore actions
const qnaList = [
    { id: 1, userId: 'b67890', question: '서비스에 대한 문의가 있습니다.', answer: '' },
    { id: 2, userId: 'b67890', question: '예약 관련 문의', answer: '안녕하세요. 예약은 홈페이지에서 가능합니다.' },
];

// const qnaList = []; // 실제 데이터가 들어갈 자리

function maskId(id) {
    return id.slice(0, 3) + '***';
}

function shortText(text, limit = 6) {
    return text.length > limit ? text.slice(0, limit) + '...' : text;
}

const container = document.getElementById('qna-list-container');

function renderQnaList() {
    container.innerHTML = '';
    const unansweredQna = qnaList.filter(qna => !qna.answer);
    const answeredQna = qnaList.filter(qna => qna.answer);

    unansweredQna.forEach(qna => {
        const item = document.createElement('div');
        item.style.cssText = `
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 20px;
            margin: 10px;
            border-radius: 10px;
            cursor: pointer;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            background-color: #ff8989;
          `;

        const questionText = document.createElement('span');
        questionText.innerText = `${shortText(qna.question)}`;
        questionText.style.fontWeight = 'bold';

        const userIdText = document.createElement('span');
        userIdText.innerText = maskId(qna.userId);
        userIdText.style.fontWeight = 'bold';

        item.appendChild(questionText);
        item.appendChild(userIdText);
        item.onclick = () => openModal(qna.id);
        container.appendChild(item);
    });

    answeredQna.forEach(qna => {
        const item = document.createElement('div');
        item.style.cssText = `
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 20px;
            margin: 10px;
            border-radius: 10px;
            cursor: pointer;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            background-color: #7abfff;
          `;

        const questionText = document.createElement('span');
        questionText.innerText = `${shortText(qna.question)}`;
        questionText.style.fontWeight = 'bold';

        const userIdText = document.createElement('span');
        userIdText.innerText = maskId(qna.userId);
        userIdText.style.fontWeight = 'bold';

        item.appendChild(questionText);
        item.appendChild(userIdText);
        item.onclick = () => openModal(qna.id);
        container.appendChild(item);
    });
}

function openModal(id) {
    const qna = qnaList.find(q => q.id === id);
    document.getElementById('modal-user-id').innerText = qna.userId;
    document.getElementById('modal-question').innerText = qna.question;

    const answerBox = document.getElementById('modal-answer');
    if (qna.answer.trim()) {
        answerBox.textContent = qna.answer;
        answerBox.style.color = '#333';
    } else {
        answerBox.textContent = '아직 답변이 없습니다.';
        answerBox.style.color = '#888';
    }

    document.getElementById('qna-modal').style.display = 'block';
    document.getElementById('modal-overlay').style.display = 'block';
}

function closeModal() {
    document.getElementById('qna-modal').style.display = 'none';
    document.getElementById('modal-overlay').style.display = 'none';
    document.getElementById('modal-answer').textContent = ''; // 초기화
}

renderQnaList();