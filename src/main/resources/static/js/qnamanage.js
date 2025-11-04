// 예시 데이터. 리스트 시각적으로 보려고 넣어놨습니다. db 연동 전 qna 확인해보고 싶으시면 주석 푸시고 확인해보세요.
const qnaList = [
    {id: 1, userId: 'a12345', question: '서비스에 대한 문의가 있습니다. 결제는 어떻게 하나요?', answer: ''},
    {id: 2, userId: 'b67890', question: '예약 관련 문의', answer: '안녕하세요. 예약은 홈페이지에서 가능합니다.'},
    {id: 3, userId: 'c11223', question: '지도에 위치가 안떠요.', answer: ''},
];
let currentQnaId = null;

function maskId(id) {
    return id.slice(0, 3) + '***';
}

function shortText(text, limit = 15) {
    return text.length > limit ? text.slice(0, limit) + '...' : text;
}

const container = document.getElementById('qna-list-container');

function renderQnaList() {
    container.innerHTML = '';

    const sortedQna = [...qnaList].sort((a, b) => {
        const isAAnswered = !!a.answer;
        const isBAnswered = !!b.answer;
        if (isAAnswered === isBAnswered) return 0;
        return isAAnswered ? 1 : -1;
    });

    sortedQna.forEach(qna => {
        const item = document.createElement('div');
        item.classList.add('qna-item');
        item.classList.add(qna.answer ? 'answered' : 'pending');

        const questionText = document.createElement('span');
        questionText.innerText = `${shortText(qna.question)}`;
        questionText.style.flexGrow = '1';
        item.appendChild(questionText);

        const userIdText = document.createElement('span');
        userIdText.setAttribute('data-full-id', qna.userId);
        userIdText.innerText = maskId(qna.userId);
        userIdText.style.fontWeight = 'bold';
        item.appendChild(userIdText);

        item.onclick = () => openModal(qna.id);

        container.appendChild(item);
    });
}

function openModal(id) {
    const qna = qnaList.find(q => q.id === id);
    if (!qna) return console.error('QnA 항목을 찾을 수 없습니다.');

    currentQnaId = id;

    document.getElementById('modal-user-id').innerText = qna.userId;
    document.getElementById('modal-question').innerText = qna.question;
    document.getElementById('modal-answer').value = qna.answer || '';
    document.getElementById('qna-modal').style.display = 'flex';
    document.getElementById('modal-overlay').style.display = 'block';

    const submitButton = document.getElementById('submit-answer-button');
    if (qna.answer) {
        submitButton.innerText = '수정하기';
    } else {
        submitButton.innerText = '답변하기';
    }
}

function submitAnswer() {
    const answer = document.getElementById('modal-answer').value;
    const qna = qnaList.find(q => q.id === currentQnaId);

    if (answer.trim()) {
        if (qna) {
            qna.answer = answer.trim();
            renderQnaList();
            closeModal();
        } else {
            console.error('답변할 QnA 항목을 찾을 수 없습니다.');
        }
    } else {
        console.error('답변을 입력해주세요.');
    }
}

function closeModal() {
    document.getElementById('qna-modal').style.display = 'none';
    document.getElementById('modal-overlay').style.display = 'none';
    document.getElementById('modal-answer').value = '';
    currentQnaId = null;
}

// 초기 렌더링
document.addEventListener('DOMContentLoaded', renderQnaList);
