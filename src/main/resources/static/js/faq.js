const faqData = [
    {
        question: "지원한 일자리의 결과는 어디서 확인하나요?",
        answer: "'내 프로필 > 지원 내역' 메뉴에서 지원한 일자리들의 진행 상황(검토 중, 합격, 불합격 등)을 확인하실 수 있습니다."
    },
    {
        question: "이력서를 수정하고 싶은데 어떻게 하나요?",
        answer: "로그인 후 '내 프로필 > 이력서 관리'에서 기존 이력서를 수정하거나 새로 등록하실 수 있습니다."
    },
    {
        question: "고령자도 지원 가능한 일자리만 보고 싶어요.",
        answer: "'일자리 찾기' 페이지 상단의 필터에서 '60세 이상 가능' 항목을 체크하면 해당 조건에 맞는 일자리만 볼 수 있습니다."
    },
    {
        question: "서류 제출이나 면접은 어떻게 진행되나요?",
        answer: "일자리마다 다르지만, 대부분 온라인 이력서로 1차 서류 검토 후 연락을 드립니다. 일부 업체는 전화 면접을 진행하기도 합니다."
    }
];

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