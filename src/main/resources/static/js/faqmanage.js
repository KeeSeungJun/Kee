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
        const question = faqItem.querySelector('.faq-question').textContent.toLowerCase();
        const answer = faqItem.querySelector('.faq-answer').textContent.toLowerCase();

        if (question.includes(searchQuery) || answer.includes(searchQuery)) {
            faqItem.style.display = 'block';
        } else {
            faqItem.style.display = 'none';
        }
    });
}

function openModal() {
    document.getElementById('faqModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('faqModal').style.display = 'none';
}

function submitFaq() {
    const title = document.getElementById('faqTitle').value;
    const content = document.getElementById('faqContent').value;

    if (title && content) {
        const faqContainer = document.querySelector('.faq-container');
        const newFaqItem = document.createElement('div');
        newFaqItem.classList.add('faq-item');
        newFaqItem.innerHTML = `
        <div class="faq-question">${title}</div>
        <div class="faq-answer">
          ${content}
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
    } else {
        alert('제목과 내용을 모두 입력해주세요!');
    }
}

function openEditModal() {
    document.getElementById('editFaqModal').style.display = 'block';
}

function closeEditModal() {
    document.getElementById('editFaqModal').style.display = 'none';
}

function editFaq(button) {
    const faqItem = button.closest('.faq-item');
    const question = faqItem.querySelector('.faq-question').textContent;

    const answerElem = faqItem.querySelector('.faq-answer');
    const answerOnly = answerElem.childNodes[0]?.textContent.trim() || ''; // 첫 텍스트 노드만 추출

    document.getElementById('editFaqTitle').value = question;
    document.getElementById('editFaqContent').value = answerOnly;

    const submitButton = document.querySelector('#editFaqModal .submit-btn-edit');
    submitButton.onclick = function () {
        faqItem.querySelector('.faq-question').textContent = document.getElementById('editFaqTitle').value;

        answerElem.innerHTML = '';
        const updatedAnswer = document.getElementById('editFaqContent').value;
        answerElem.textContent = updatedAnswer;

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

function deleteFaq(button) {
    if (confirm('정말 삭제하시겠습니까?')) {
        const faqItem = button.closest('.faq-item');
        faqItem.remove();
    }
}

document.addEventListener('DOMContentLoaded', function () {
    setFaqToggleEvents(); // 기존 항목 이벤트 설정
});