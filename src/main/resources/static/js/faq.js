function setFaqToggleEvents() {
    document.querySelectorAll('.faq-question').forEach(function(question) {
        question.removeEventListener('click', toggleAnswer);
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

document.addEventListener('DOMContentLoaded', function () {
    setFaqToggleEvents(); // 기존 항목 이벤트 설정
});

document.querySelectorAll('.faq-question').forEach(question => {
    question.addEventListener('click', () => {
        const answer = question.nextElementSibling;
        if (answer.classList.contains('show')) {
            answer.classList.remove('show');
            answer.classList.add('hide');
        } else {
            answer.classList.remove('hide');
            answer.classList.add('show');
        }
    });
});