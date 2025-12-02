// 1. 데이터 변수
let faqData = [];

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
    loadFaqData();
});

// 1-1. DB에서 FAQ 데이터 로드
async function loadFaqData() {
    try {
        const response = await fetch('/api/faqs');
        const data = await response.json();
        
        if (data.faqs) {
            faqData = data.faqs.map(faq => ({
                id: faq.faqNo,
                category: faq.faqCategory,
                question: faq.faqQuestion,
                answer: faq.faqAnswer
            }));
            
            console.log('FAQ 데이터 로드 완료:', faqData.length + '개');
            renderFaqList();
        }
    } catch (error) {
        console.error('FAQ 데이터 로드 실패:', error);
        showToast('데이터를 불러오는데 실패했습니다.', 'error');
    }
}

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
async function confirmSubmitFaq() {
    const category = document.getElementById('faqCategory').value;
    const question = document.getElementById('faqQuestion').value;
    const answer = document.getElementById('faqAnswer').value;

    try {
        if (currentEditId) {
            // 수정
            const response = await fetch(`/api/faqs/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    faqCategory: category,
                    faqQuestion: question,
                    faqAnswer: answer
                })
            });

            const result = await response.json();
            
            if (result.success) {
                showToast("수정되었습니다.", "success");
                await loadFaqData(); // 데이터 다시 로드
            } else {
                showToast(result.message || "수정에 실패했습니다.", "error");
            }
        } else {
            // 등록
            const response = await fetch('/api/faqs', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    faqCategory: category,
                    faqQuestion: question,
                    faqAnswer: answer,
                    createdBy: 'admin'
                })
            });

            const result = await response.json();
            
            if (result.success) {
                showToast("등록되었습니다.", "success");
                await loadFaqData(); // 데이터 다시 로드
            } else {
                showToast(result.message || "등록에 실패했습니다.", "error");
            }
        }

        closeRegisterConfirmModal();
        closeModal();
    } catch (error) {
        console.error('FAQ 저장 실패:', error);
        showToast('저장 중 오류가 발생했습니다.', 'error');
    }
}

// 5. 삭제 기능 (모달 호출)
function deleteFaq(id) {
    deleteTargetId = id;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

async function confirmDelete() {
    if (!deleteTargetId) return;

    try {
        const response = await fetch(`/api/faqs/${deleteTargetId}`, {
            method: 'DELETE'
        });

        const result = await response.json();
        
        if (result.success) {
            showToast("삭제되었습니다.", "info");
            await loadFaqData(); // 데이터 다시 로드
            closeDeleteModal();
        } else {
            showToast(result.message || "삭제에 실패했습니다.", "error");
        }
    } catch (error) {
        console.error('FAQ 삭제 실패:', error);
        showToast('삭제 중 오류가 발생했습니다.', 'error');
    }
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