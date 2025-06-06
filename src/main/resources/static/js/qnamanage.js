// 예시 데이터. 리스트 시각적으로 보려고 넣어놨습니다. db 연동 전 qna 확인해보고 싶으시면 주석 푸시고 확인해보세요.
// const qnaList = [
//   { id: 1, userId: 'a12345', question: '서비스에 대한 문의가 있습니다.', answer: '' },
//   { id: 2, userId: 'b67890', question: '예약 관련 문의', answer: '안녕하세요. 예약은 홈페이지에서 가능합니다.' },
// ];

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
        item.style.display = 'flex';
        item.style.justifyContent = 'space-between';
        item.style.alignItems = 'center';
        item.style.padding = '12px 20px';
        item.style.margin = '10px';
        item.style.borderRadius = '10px';
        item.style.cursor = 'pointer';
        item.style.boxShadow = '0 2px 6px rgba(0, 0, 0, 0.1)';
        item.style.backgroundColor = '#ff8989';

        const questionText = document.createElement('span');
        questionText.innerText = `${shortText(qna.question)}`;
        questionText.style.fontWeight = 'bold';
        item.appendChild(questionText);

        const userIdText = document.createElement('span');
        userIdText.innerText = maskId(qna.userId);
        userIdText.style.fontWeight = 'bold';
        item.appendChild(userIdText);

        item.onclick = () => openModal(qna.id);

        container.appendChild(item);
    });

    answeredQna.forEach(qna => {
        const item = document.createElement('div');
        item.style.display = 'flex';
        item.style.justifyContent = 'space-between';
        item.style.alignItems = 'center';
        item.style.padding = '12px 20px';
        item.style.margin = '10px';
        item.style.borderRadius = '10px';
        item.style.cursor = 'pointer';
        item.style.boxShadow = '0 2px 6px rgba(0, 0, 0, 0.1)';
        item.style.backgroundColor = '#7abfff';

        const questionText = document.createElement('span');
        questionText.innerText = `${shortText(qna.question)}`;
        questionText.style.fontWeight = 'bold';
        item.appendChild(questionText);

        const userIdText = document.createElement('span');
        userIdText.innerText = maskId(qna.userId);
        userIdText.style.fontWeight = 'bold';
        item.appendChild(userIdText);

        item.onclick = () => openModal(qna.id);

        container.appendChild(item);
    });
}

function openModal(id) {
    const qna = qnaList.find(q => q.id === id);
    document.getElementById('modal-user-id').innerText = qna.userId;
    document.getElementById('modal-question').innerText = qna.question;
    document.getElementById('modal-answer').value = qna.answer || '';
    document.getElementById('qna-modal').style.display = 'block';

    const submitButton = document.getElementById('submit-answer-button');
    if (qna.answer) {
        submitButton.innerText = '수정하기';
    } else {
        submitButton.innerText = '답변하기';
    }

    document.getElementById('modal-overlay').style.display = 'block';
}

function submitAnswer() {
    const answer = document.getElementById('modal-answer').value;
    const userId = document.getElementById('modal-user-id').innerText;
    const qna = qnaList.find(q => q.id === id);

    if (answer.trim()) {
        qna.answer = answer;
        renderQnaList();
        closeModal();
    } else {
        alert('답변을 입력해주세요.');
    }
}

function closeModal() {
    document.getElementById('qna-modal').style.display = 'none';
    document.getElementById('modal-overlay').style.display = 'none';
    document.getElementById('modal-answer').value = '';
}

// 처음 렌더링
renderQnaList();