function setFaqToggleEvents() {
    document.querySelectorAll('.faq-question').forEach(function(question) {
        question.removeEventListener('click', toggleAnswer); // 중복 방지
        question.addEventListener('click', toggleAnswer);
    });
}

function toggleAnswer(e) {
    const answer = e.target.nextElementSibling;
    if (answer) {
        answer.classList.toggle('show');
    }
}

function filterFaqs() {
    const searchQuery = document.getElementById('faqSearch').value.toLowerCase();
    const faqItems = document.querySelectorAll('.faq-item');

    faqItems.forEach(function(faqItem) {
        // 아이콘 등을 제외한 텍스트만 추출하도록 수정
        const questionText = faqItem.querySelector('.faq-question').textContent.trim().toLowerCase();

        // faq-answer에서 텍스트만 추출 (버튼 등 제외)
        const answerElem = faqItem.querySelector('.faq-answer');
        const answerText = answerElem ? Array.from(answerElem.childNodes)
            .filter(node => node.nodeType === 3) // Text node만 필터링
            .map(node => node.textContent.trim())
            .join(' ')
            .toLowerCase() : '';

        if (questionText.includes(searchQuery) || answerText.includes(searchQuery)) {
            faqItem.style.display = 'block';
        } else {
            faqItem.style.display = 'none';
        }
    });
}

// (수정) display: 'block' 대신 display: 'flex' 사용
function openModal() {
    document.getElementById('faqModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('faqModal').style.display = 'none';
}

function submitFaq() {
    const title = document.getElementById('faqTitle').value;
    const content = document.getElementById('faqContent').value;

    // alert() 대신 console.error 사용 (Canvas 규정 준수)
    if (!title || !content) {
        console.error('제목과 내용을 모두 입력해주세요!');
        return;
    }

    const faqContainer = document.querySelector('.faq-container');
    const newFaqItem = document.createElement('div');
    newFaqItem.classList.add('faq-item');
    newFaqItem.innerHTML = `
        <div class="faq-question">
          <span class="q-icon">Q.</span>${title}
        </div>
        <div class="faq-answer">
          <span class="a-icon" style="display:none;">A.</span>${content}
          <div class="faq-actions">
            <button class="edit-btn" onclick="editFaq(this)">수정하기</button>
            <button class="delete-btn" onclick="deleteFaq(this)">삭제하기</button>
          </div>
        </div>
      `;
    faqContainer.appendChild(newFaqItem);

    setFaqToggleEvents();

    document.getElementById('faqTitle').value = '';
    document.getElementById('faqContent').value = '';
    closeModal();

}

// (수정) display: 'block' 대신 display: 'flex' 사용
function openEditModal() {
    document.getElementById('editFaqModal').style.display = 'flex';
}

function closeEditModal() {
    document.getElementById('editFaqModal').style.display = 'none';
}

function editFaq(button) {
    // alert() 대신 console.error 사용
    const faqItem = button.closest('.faq-item');
    if (!faqItem) {
        console.error("FAQ item not found for editing.");
        return;
    }

    // Q. 아이콘 태그 제거하고 순수한 텍스트만 추출
    let question = faqItem.querySelector('.faq-question').textContent.trim();
    if (question.startsWith('Q.')) {
        question = question.substring(2).trim();
    }


    const answerElem = faqItem.querySelector('.faq-answer');

    // 답변에서 텍스트 노드만 추출 (A. 아이콘과 버튼 그룹 제외)
    let answerOnly = Array.from(answerElem.childNodes)
        .filter(node => node.nodeType === 3) // 텍스트 노드
        .map(node => node.textContent.trim())
        .join(' ');

    if (answerOnly.startsWith('A.')) {
        answerOnly = answerOnly.substring(2).trim();
    }

    document.getElementById('editFaqTitle').value = question;
    document.getElementById('editFaqContent').value = answerOnly;

    const submitButton = document.querySelector('#editFaqModal .submit-btn-edit');
    submitButton.onclick = function () {
        const newTitle = document.getElementById('editFaqTitle').value;
        const newContent = document.getElementById('editFaqContent').value;

        // Q. 아이콘을 포함하여 다시 설정
        faqItem.querySelector('.faq-question').innerHTML = `<span class="q-icon">Q.</span>${newTitle}`;

        answerElem.innerHTML = `<span class="a-icon" style="display:none;">A.</span>${newContent}`;

        const actions = document.createElement('div');
        actions.className = 'faq-actions';
        actions.innerHTML = `
            <button class="edit-btn" onclick="editFaq(this)">수정하기</button>
            <button class="delete-btn" onclick="deleteFaq(this)">삭제하기</button>
        `;
        answerElem.appendChild(actions);

        closeEditModal();
    };

    openEditModal();
}

// (수정) confirm() 대신 console.error 사용 (Canvas 규정 준수)
function deleteFaq(button) {
    // 경고 메시지 대신 바로 삭제하거나, 사용자 정의 모달 UI를 사용해야 하지만
    // 임시로 메시지를 표시합니다.
    console.warn('FAQ를 삭제합니다.');
    const faqItem = button.closest('.faq-item');
    faqItem.remove();
}

document.addEventListener('DOMContentLoaded', function () {
    setFaqToggleEvents(); // 기존 항목 이벤트 설정
    // filterFaqs가 input의 onkeyup에 있으므로 DOMContentLoaded에서 호출 필요 없음
});
