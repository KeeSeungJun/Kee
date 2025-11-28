// 1. 샘플 하드코딩 데이터
let faqData = [
    {id: 1, category: 'account', question: "회원가입은 어떻게 하나요?", answer: "메인 화면의 '회원가입' 버튼을 통해 가능합니다."},
    {id: 2, category: 'apply', question: "일자리 지원 결과 확인", answer: "내 프로필 > 지원 내역에서 확인 가능합니다."},
    {id: 3, category: 'salary', question: "급여 지급일은 언제인가요?", answer: "각 업체별 근로 계약서에 명시되어 있습니다."},
    {id: 4, category: 'etc', question: "앱 오류 신고 방법", answer: "문의하기 게시판을 이용해 주세요."}
];

// 카테고리 표시용 맵
const categoryMap = {
    'account': {text: '회원가입/계정', class: 'cat-account'},
    'apply': {text: '지원/면접', class: 'cat-apply'},
    'salary': {text: '급여/복지', class: 'cat-salary'},
    'etc': {text: '기타 문의', class: ''}
};

let currentEditId = null;
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', () => {
    renderFaqList();
});

// 2. 리스트 렌더링
function renderFaqList() {
    const container = document.getElementById('faqListContainer');
    const keyword = document.getElementById('faqSearch').value.toLowerCase();
    container.innerHTML = '';

    const filteredData = faqData.filter(faq => {
        return faq.question.toLowerCase().includes(keyword) ||
            faq.answer.toLowerCase().includes(keyword);
    });

    if (filteredData.length === 0) {
        container.innerHTML = '<div style="padding:40px; text-align:center; color:#888;">데이터가 없습니다.</div>';
        return;
    }

    filteredData.forEach(faq => {
        const div = document.createElement('div');
        div.className = 'faq-item';

        const catInfo = categoryMap[faq.category] || {text: faq.category, class: ''};

        div.innerHTML = `
            <div class="col category"><span class="${catInfo.class}">${catInfo.text}</span></div>
            <div class="col question">${faq.question}</div>
            <div class="col answer">${faq.answer}</div>
            <div class="col actions">
                <button class="action-icon-btn" onclick="openModal('edit', ${faq.id})" title="수정">
                    <i class="fa-solid fa-pen"></i>
                </button>
                <button class="action-icon-btn delete" onclick="deleteFaq(${faq.id})" title="삭제">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </div>
        `;
        container.appendChild(div);
    });
}

function filterFaqs() {
    renderFaqList();
}

// 3. 모달 열기 (등록/수정)
function openModal(mode, id = null) {
    const modal = document.getElementById('faqModal');
    const title = document.getElementById('modalTitle');
    const deleteBtn = document.getElementById('deleteBtn');

    document.getElementById('faqForm').reset();

    if (mode === 'edit' && id) {
        const faq = faqData.find(f => f.id === id);
        if (!faq) return;

        currentEditId = id;
        title.innerText = 'FAQ 수정';
        deleteBtn.style.display = 'block'; // 수정 모드일 때만 삭제 버튼 보임

        document.getElementById('faqCategory').value = faq.category;
        document.getElementById('faqQuestion').value = faq.question;
        document.getElementById('faqAnswer').value = faq.answer;
    } else {
        currentEditId = null;
        title.innerText = 'FAQ 등록';
        deleteBtn.style.display = 'none';
    }

    modal.style.display = 'flex';
}

function closeModal() {
    document.getElementById('faqModal').style.display = 'none';
    currentEditId = null; // [중요] 닫을 때 ID 초기화됨
}

// 4. 저장 전 확인 (모달 띄우기)
function preSubmitFaq() {
    const question = document.getElementById('faqQuestion').value;
    const answer = document.getElementById('faqAnswer').value;

    if (!question || !answer) {
        showToast("질문과 답변을 모두 입력해주세요.", "error");
        return;
    }

    // 등록/수정 확인 모달 띄우기
    document.getElementById('registerConfirmModal').style.display = 'flex';
}

function closeRegisterConfirmModal() {
    document.getElementById('registerConfirmModal').style.display = 'none';
}

// 실제 저장 로직
function confirmSubmitFaq() {
    const category = document.getElementById('faqCategory').value;
    const question = document.getElementById('faqQuestion').value;
    const answer = document.getElementById('faqAnswer').value;

    if (currentEditId) {
        // 수정
        const index = faqData.findIndex(f => f.id === currentEditId);
        if (index > -1) {
            faqData[index] = {id: currentEditId, category, question, answer};
            showToast("수정되었습니다.", "success");
        }
    } else {
        // 등록
        const newId = faqData.length > 0 ? Math.max(...faqData.map(f => f.id)) + 1 : 1;
        faqData.unshift({id: newId, category, question, answer});
        showToast("등록되었습니다.", "success");
    }

    renderFaqList();
    closeRegisterConfirmModal();
    closeModal();
}

// 5. 삭제 기능 (모달 호출)
function deleteFaq(id) {
    deleteTargetId = id;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

function confirmDelete() {
    if (deleteTargetId) {
        faqData = faqData.filter(f => f.id !== deleteTargetId);
        renderFaqList();
        showToast("삭제되었습니다.", "info");
    }
    closeDeleteModal();
}

function closeDeleteModal() {
    document.getElementById('deleteConfirmModal').style.display = 'none';
    deleteTargetId = null;
}

// 수정 모달 내 삭제 버튼 클릭 시
function deleteFaqFromModal() {
    if (currentEditId) {
        const idToDelete = currentEditId; // ID 값을 미리 백업해둠
        closeModal(); // 모달 닫기
        deleteFaq(idToDelete); // 백업한 ID로 삭제 모달 호출
    }
}