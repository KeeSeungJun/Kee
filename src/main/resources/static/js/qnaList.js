// 1. 하드코딩된 예시 데이터
let qnaList = [
    {
        id: 1,
        date: '2024.05.20',
        question: '제가 지원한 일자리에 언제쯤 연락이 올까요?',
        answer: "안녕하세요. 지원하신 공고의 담당자가 서류 검토 후 1주일 이내로 개별 연락드릴 예정입니다. 조금만 기다려주세요.",
        status: 'answered'
    },
    {id: 2, date: '2024.05.22', question: '비밀번호 변경이 안 됩니다. 오류가 떠요.', answer: '', status: 'waiting'},
    {
        id: 3,
        date: '2024.05.23',
        question: '프로필 내역을 변경하고 싶습니다.',
        answer: "프로필 내역 변경은 '내 프로필 > 프로필 수정'에서 직접 변경하시거나, 고객센터로 연락주시면 처리해 드리겠습니다.",
        status: 'answered'
    },
    {id: 4, date: '2024.05.25', question: '주말에도 일자리 추천 알림이 오나요?', answer: '', status: 'waiting'}
];

let currentFilter = 'all';
let editingId = null;
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', () => {
    renderList();
});

// 2. 리스트 렌더링
function renderList() {
    const container = document.getElementById('qna-list-container');
    container.innerHTML = '';

    const filteredList = qnaList.filter(item => {
        if (currentFilter === 'all') return true;
        return item.status === currentFilter;
    });

    filteredList.sort((a, b) => b.id - a.id);

    if (filteredList.length === 0) {
        container.innerHTML = '<div style="text-align:center; padding:40px; color:#888;">문의 내역이 없습니다.</div>';
        return;
    }

    filteredList.forEach(item => {
        const card = document.createElement('div');
        card.className = 'qna-item';
        const statusClass = item.status === 'answered' ? 'answered' : 'waiting';
        const statusText = item.status === 'answered' ? '답변완료' : '답변대기';

        card.innerHTML = `
            <div class="qna-header">
                <div class="qna-preview">${item.question}</div>
                <span class="status-badge ${statusClass}">${statusText}</span>
            </div>
            <div class="qna-meta">
                <span class="qna-date">${item.date}</span>
            </div>
        `;

        card.onclick = () => handleItemClick(item);
        container.appendChild(card);
    });
}

function filterList(filterType, btnElement) {
    currentFilter = filterType;
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    btnElement.classList.add('active');
    renderList();

    document.getElementById('detail-view').style.display = 'none';
    document.getElementById('empty-detail-view').style.display = 'flex';
}

// 3. 상세 보기
function handleItemClick(item) {
    const isMobile = window.innerWidth <= 768;
    if (isMobile) openMobileDetailModal(item);
    else showPcDetailView(item);
}

function showPcDetailView(item) {
    document.getElementById('empty-detail-view').style.display = 'none';
    document.getElementById('detail-view').style.display = 'flex';

    const statusBadge = document.getElementById('pc-status-badge');
    statusBadge.innerText = item.status === 'answered' ? '답변완료' : '답변대기';
    statusBadge.className = `detail-status-badge ${item.status === 'answered' ? '' : 'waiting-badge'}`;

    document.getElementById('pc-question-title').innerText = item.question.length > 20 ? item.question.substring(0, 20) + '...' : item.question;
    document.getElementById('pc-date').innerText = item.date;
    document.getElementById('pc-question-text').innerText = item.question;

    const answerArea = document.getElementById('pc-answer-area');
    if (item.status === 'answered') {
        answerArea.style.display = 'block';
        document.getElementById('pc-answer-text').innerText = item.answer;
    } else {
        answerArea.style.display = 'none';
    }

    const footer = document.getElementById('pc-action-buttons');
    let buttonsHtml = '';
    if (item.status === 'waiting') {
        buttonsHtml += `<button class="action-btn btn-edit" onclick="editInquiry(${item.id})">수정하기</button>`;
    }
    buttonsHtml += `<button class="action-btn btn-delete" onclick="deleteInquiry(${item.id})">삭제하기</button>`;
    footer.innerHTML = buttonsHtml;
}

function openMobileDetailModal(item) {
    const modal = document.getElementById('mobile-detail-modal');

    document.getElementById('mb-status-badge').innerText = item.status === 'answered' ? '답변완료' : '답변대기';
    document.getElementById('mb-date').innerText = item.date;
    document.getElementById('mb-question-text').innerText = item.question;

    const answerArea = document.getElementById('mb-answer-area');
    if (item.status === 'answered') {
        answerArea.style.display = 'block';
        document.getElementById('mb-answer-text').innerText = item.answer;
    } else {
        answerArea.style.display = 'none';
    }

    const footer = document.getElementById('mb-action-buttons');
    let buttonsHtml = '';
    if (item.status === 'waiting') {
        buttonsHtml += `<button class="secondary-modal-btn" onclick="editInquiry(${item.id})">수정</button>`;
    }
    buttonsHtml += `<button class="secondary-modal-btn" style="color:#e74c3c;" onclick="deleteInquiry(${item.id})">삭제</button>`;
    buttonsHtml += `<button class="primary-modal-btn" onclick="closeMobileModal()">닫기</button>`;

    footer.innerHTML = buttonsHtml;
    modal.style.display = 'flex';
}

function closeMobileModal() {
    document.getElementById('mobile-detail-modal').style.display = 'none';
}

// 4. 작성/수정
function openWriteModal(mode = 'create') {
    const modal = document.getElementById('write-modal');
    const title = document.getElementById('write-modal-title');
    const btn = document.getElementById('modal-submit-btn');
    const deleteBtn = document.getElementById('modal-delete-btn'); // 삭제 버튼
    const textarea = document.getElementById('new-question-text');

    if (mode === 'create') {
        editingId = null;
        title.innerText = "새로운 문의 작성";
        btn.innerText = "등록하기";
        textarea.value = '';
        if (deleteBtn) deleteBtn.style.display = 'none'; // 등록 시 숨김
    } else {
        title.innerText = "문의 내용 수정";
        btn.innerText = "수정완료";
        if (deleteBtn) deleteBtn.style.display = 'block'; // 수정 시 보임
    }
    modal.style.display = 'flex';
}

function closeWriteModal() {
    document.getElementById('write-modal').style.display = 'none';
}

function editInquiry(id) {
    const item = qnaList.find(q => q.id === id);
    if (!item) return;

    closeMobileModal();
    openWriteModal('edit');
    editingId = id;
    document.getElementById('new-question-text').value = item.question;
}

function preSubmitInquiry() {
    const text = document.getElementById('new-question-text').value.trim();
    if (!text) {
        showToast("내용을 입력해주세요.", "error");
        return;
    }
    document.getElementById('registerConfirmModal').style.display = 'flex';
}

function closeRegisterConfirmModal() {
    document.getElementById('registerConfirmModal').style.display = 'none';
}

function confirmSubmitInquiry() {
    const text = document.getElementById('new-question-text').value.trim();

    if (editingId) {
        const item = qnaList.find(q => q.id === editingId);
        if (item) {
            item.question = text;
            showToast("수정되었습니다.", "success");

            const pcTitle = document.getElementById('pc-question-text');
            if (pcTitle && pcTitle.innerText !== '') showPcDetailView(item);
        }
    } else {
        const newId = qnaList.length > 0 ? Math.max(...qnaList.map(i => i.id)) + 1 : 1;
        const today = new Date().toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit'
        }).replace(/\./g, '.');

        qnaList.unshift({
            id: newId,
            date: today,
            question: text,
            answer: '',
            status: 'waiting'
        });
        showToast("문의가 등록되었습니다.", "success");
    }

    renderList();
    closeRegisterConfirmModal();
    closeWriteModal();
}

// 5. 삭제 기능
function deleteInquiry(id) {
    deleteTargetId = id;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

function confirmDelete() {
    if (deleteTargetId) {
        qnaList = qnaList.filter(item => item.id !== deleteTargetId);
        renderList();

        document.getElementById('detail-view').style.display = 'none';
        document.getElementById('empty-detail-view').style.display = 'flex';
        closeMobileModal();

        showToast("삭제되었습니다.", "info");
    }
    closeDeleteModal();
}

function closeDeleteModal() {
    document.getElementById('deleteConfirmModal').style.display = 'none';
    deleteTargetId = null;
}

// 수정 모달 내 삭제 버튼
function deleteInquiryFromModal() {
    if (editingId) {
        const idToDelete = editingId;
        closeWriteModal();
        deleteInquiry(idToDelete);
    }
}