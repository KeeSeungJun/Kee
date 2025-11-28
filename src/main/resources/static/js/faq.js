// 1. 예시 데이터 (수정됨)
const faqData = [
    // [회원가입/계정]
    { category: 'account', question: "회원가입은 어떻게 하나요?", answer: "메인 화면의 '회원가입' 버튼을 누르신 후, 이름과 전화번호 등 필수 정보를 입력하시면 가입이 완료됩니다." },
    { category: 'account', question: "비밀번호를 잊어버렸어요.", answer: "로그인 화면 하단의 '비밀번호 찾기'를 눌러주세요. 가입하신 휴대폰 번호로 인증 후 비밀번호를 재설정할 수 있습니다." },
    { category: 'account', question: "계정을 탈퇴하고 싶어요.", answer: "'내 프로필 > 계정 탈퇴' 메뉴에서 진행하실 수 있습니다. 탈퇴 시 모든 정보가 삭제되니 신중하게 결정해주세요." },

    // [지원/면접] - 내용 수정됨
    { category: 'apply', question: "일자리 지원은 어떻게 하나요?", answer: "메인 화면의 '일자리 추천받기' 메뉴에서 마음에 드는 공고를 선택한 후, 상세 화면 하단의 '신청하기' 버튼을 누르면 지원이 완료됩니다." },
    { category: 'apply', question: "지원 후 연락은 언제 오나요?", answer: "업체 사정에 따라 다르지만, 보통 서류 검토 후 1주일 이내에 등록된 연락처로 개별 연락을 드립니다." },
    { category: 'apply', question: "면접 복장은 어떻게 해야 하나요?", answer: "단정하고 깔끔한 복장을 추천드립니다. 별도의 복장 규정이 있는 경우 채용 공고에 명시되어 있습니다." },

    // [급여/복지]
    { category: 'salary', question: "급여는 언제 들어오나요?", answer: "급여 지급일은 각 업체마다 다릅니다. 채용 공고의 상세 내용을 확인하시거나, 근로 계약서 작성 시 확인 가능합니다." },
    { category: 'salary', question: "주휴수당을 받을 수 있나요?", answer: "주 15시간 이상 근무하고 개근한 경우 주휴수당 지급 대상이 됩니다. 자세한 내용은 업체 담당자에게 문의해주세요." },
    { category: 'salary', question: "4대보험 가입이 가능한가요?", answer: "월 60시간 이상 근무하는 경우 4대보험 가입이 의무입니다. 단, 일자리 조건에 따라 다를 수 있습니다." },

    // [기타 문의]
    { category: 'etc', question: "앱 사용 중 오류가 발생했어요.", answer: "오류 화면을 캡처하여 '문의하기' 게시판에 남겨주시면 빠르게 확인 후 조치해 드리겠습니다." },
    { category: 'etc', question: "고객센터 운영 시간은 언제인가요?", answer: "평일 오전 9시부터 오후 6시까지 운영됩니다. (점심시간 12:00 ~ 13:00 제외)" }
];

// 초기 실행
document.addEventListener('DOMContentLoaded', function () {
    renderFaqs('all'); // 전체 보기로 시작
});

// 2. 카테고리 선택 및 렌더링 함수
function selectCategory(element, category) {
    // 탭 스타일 변경
    const items = document.querySelectorAll('.category-list li');
    items.forEach(item => item.classList.remove('active'));
    element.classList.add('active');

    // 검색어 초기화
    document.getElementById('faqSearch').value = '';

    // 데이터 렌더링
    renderFaqs(category);
}

// FAQ 리스트 그리기
function renderFaqs(category) {
    const container = document.getElementById('faqListContainer');
    container.innerHTML = ''; // 기존 내용 비우기

    // 데이터 필터링
    const filteredData = category === 'all'
        ? faqData
        : faqData.filter(item => item.category === category);

    if (filteredData.length === 0) {
        container.innerHTML = '<div style="text-align:center; padding:40px; color:#888;">해당 카테고리에 등록된 질문이 없습니다.</div>';
        return;
    }

    // HTML 생성 및 추가
    filteredData.forEach(faq => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'faq-item';

        itemDiv.innerHTML = `
            <div class="faq-question">
                <span class="q-mark">Q.</span>
                <span class="q-text">${faq.question}</span>
                <i class="fa-solid fa-chevron-down toggle-icon"></i>
            </div>
            <div class="faq-answer">
                <div class="answer-content">${faq.answer}</div>
            </div>
        `;

        // 아코디언 이벤트 리스너 추가
        itemDiv.querySelector('.faq-question').addEventListener('click', function() {
            toggleAnswer(this);
        });

        container.appendChild(itemDiv);
    });
}

// 3. 아코디언 토글 함수
function toggleAnswer(questionElement) {
    const faqItem = questionElement.parentElement;
    const answerEl = faqItem.querySelector('.faq-answer');

    // 현재 항목 토글
    const isActive = faqItem.classList.contains('active');

    if (!isActive) {
        faqItem.classList.add('active');
        answerEl.classList.add('show');
    } else {
        faqItem.classList.remove('active');
        answerEl.classList.remove('show');
    }
}

// 4. 검색 필터링
function filterFaqs() {
    const searchQuery = document.getElementById('faqSearch').value.toLowerCase();
    const faqItems = document.querySelectorAll('.faq-item');

    faqItems.forEach(function(item) {
        const question = item.querySelector('.q-text').textContent.toLowerCase();
        const answer = item.querySelector('.answer-content').textContent.toLowerCase();

        if (question.includes(searchQuery) || answer.includes(searchQuery)) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}