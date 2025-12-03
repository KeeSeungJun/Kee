// 1. 데이터 변수 (초기화)
let qnaList = [];
let currentUser = null; // 현재 로그인한 사용자 정보

let currentFilter = 'all';
let editingId = null;
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', () => {
    fetchCurrentUser(); // 사용자 정보 가져오기
    loadQnaData();      // QnA 목록 가져오기
});

// 현재 로그인한 사용자 정보 조회
async function fetchCurrentUser() {
    try {
        const response = await fetch('/api/user/me');
        if (response.ok) {
            currentUser = await response.json();
            console.log("로그인 사용자 정보:", currentUser); // 디버깅용 로그 확인
        } else {
            console.warn("비로그인 상태입니다.");
        }
    } catch (error) {
        console.error("사용자 정보 로드 실패", error);
    }
}

// 2. 서버에서 QnA 데이터 로드
async function loadQnaData() {
    try {
        // 필터링 상태에 따라 파라미터 전달 가능 (현재는 전체 로드 후 프론트 필터링 유지)
        const response = await fetch('/api/qnas');
        const data = await response.json();

        if (data.qnas) {
            // DB 데이터를 프론트엔드 형식으로 매핑
            qnaList = data.qnas.map(q => ({
                id: q.qnaNo,
                date: q.createdAt ? q.createdAt.substring(0, 10).replace(/-/g, '.') : '-',
                question: q.qnaContent,
                answer: q.qnaAnswer || '',
                // DB 상태(WAITING/COMPLETED) -> 프론트 상태(waiting/answered) 변환
                status: q.qnaStatus === 'COMPLETED' ? 'answered' : 'waiting',
                usrId: q.usrId // 작성자 ID 저장
            }));

            renderList();
        }
    } catch (error) {
        console.error('QnA 데이터 로드 실패:', error);
        showToast('데이터를 불러오는데 실패했습니다.', 'error');
    }
}

// 3. 리스트 렌더링
function renderList() {
    const container = document.getElementById('qna-list-container');
    container.innerHTML = '';

    const filteredList = qnaList.filter(item => {
        if (currentFilter === 'all') return true;
        return item.status === currentFilter;
    });

    // 최신순 정렬 (ID 역순)
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

    // 상세 뷰 초기화
    document.getElementById('detail-view').style.display = 'none';
    document.getElementById('empty-detail-view').style.display = 'flex';
}

// 4. 상세 보기
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

    // 본인 글이거나 관리자일 때만 수정/삭제 버튼 노출 (여기서는 일단 모두 노출하되, 답변 완료시 수정 불가 처리)
    let buttonsHtml = '';

    // 답변 대기 상태일 때만 수정 가능
    if (item.status === 'waiting') {
        buttonsHtml += `<button class="action-btn btn-edit" onclick="editInquiry(${item.id})">수정하기</button>`;
        buttonsHtml += `<button class="action-btn btn-delete" onclick="deleteInquiry(${item.id})">삭제하기</button>`;
    } else {
        // 답변 완료 시 삭제만 가능
        buttonsHtml += `<button class="action-btn btn-delete" onclick="deleteInquiry(${item.id})">삭제하기</button>`;
    }

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
        buttonsHtml += `<button class="secondary-modal-btn" style="color:#e74c3c;" onclick="deleteInquiry(${item.id})">삭제</button>`;
    } else {
        buttonsHtml += `<button class="secondary-modal-btn" style="color:#e74c3c;" onclick="deleteInquiry(${item.id})">삭제</button>`;
    }
    buttonsHtml += `<button class="primary-modal-btn" onclick="closeMobileModal()">닫기</button>`;

    footer.innerHTML = buttonsHtml;
    modal.style.display = 'flex';
}

function closeMobileModal() {
    document.getElementById('mobile-detail-modal').style.display = 'none';
}

// 5. 작성/수정 모달 관련
function openWriteModal(mode = 'create') {
    const modal = document.getElementById('write-modal');
    const title = document.getElementById('write-modal-title');
    const btn = document.getElementById('modal-submit-btn');
    const deleteBtn = document.getElementById('modal-delete-btn');
    const textarea = document.getElementById('new-question-text');

    if (mode === 'create') {
        editingId = null;
        title.innerText = "새로운 문의 작성";
        btn.innerText = "등록하기";
        textarea.value = '';
        if (deleteBtn) deleteBtn.style.display = 'none';
    } else {
        title.innerText = "문의 내용 수정";
        btn.innerText = "수정완료";
        if (deleteBtn) deleteBtn.style.display = 'block';
    }
    modal.style.display = 'flex';
}

function closeWriteModal() {
    document.getElementById('write-modal').style.display = 'none';
}

function editInquiry(id) {
    const item = qnaList.find(q => q.id === id);
    if (!item) return;

    closeMobileModal(); // 모바일 상세창 닫기
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

// [핵심 수정] 서버로 데이터 전송 (등록/수정)
async function confirmSubmitInquiry() {
    const text = document.getElementById('new-question-text').value.trim();

    // 제목 자동 생성 (내용의 앞부분 20자)
    const title = text.length > 20 ? text.substring(0, 20) + "..." : text;

    // [수정 포인트] 유저 ID 처리 (Snake Case 대응: user_id 또는 userId 둘 다 확인)
    const userId = currentUser ? (currentUser.user_id || currentUser.userId) : null;

    if (!userId) {
        console.log("로그인 정보 확인 불가:", currentUser); // 디버깅용
        showToast("로그인 정보가 없습니다. 다시 로그인해주세요.", "error");
        closeRegisterConfirmModal();
        return;
    }

    try {
        let response;
        if (editingId) {
            // 수정 (PUT)
            // 주의: 백엔드에 PUT /api/qnas/{id} 매핑이 없으면 405 에러가 날 수 있음.
            // 현재 컨트롤러 구조상 수정은 관리자 답변(PUT /api/qnas/{id}/answer)만 있을 수 있으니 확인 필요.
            // 사용자 수정 기능이 없다면, 새로 등록하게 하거나 백엔드 추가 필요.
            // 일단 요청을 보내봅니다.
            response = await fetch(`/api/qnas/${editingId}`, {
                method: 'PUT', // 만약 405 Method Not Allowed 뜨면 백엔드 확인 필요
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    qnaTitle: title,
                    qnaContent: text
                })
            });
        } else {
            // 등록 (POST)
            response = await fetch('/api/qnas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    usrId: userId,
                    qnaTitle: title,
                    qnaContent: text
                })
            });
        }

        // 응답 처리
        if (response.ok) {
            const result = await response.json();
            // DefaultResponse 구조(code, message, result map)에 따라 성공 체크
            if (result.code === 200 || result.success) {
                showToast(editingId ? "수정되었습니다." : "문의가 등록되었습니다.", "success");
                await loadQnaData(); // 목록 새로고침

                // 수정 상태였으면 상세 뷰도 업데이트 시도
                if (editingId) {
                    const item = qnaList.find(q => q.id === editingId);
                    if (item) showPcDetailView(item);
                }
            } else {
                showToast(result.message || "작업에 실패했습니다.", "error");
            }
        } else {
            showToast("서버 오류가 발생했습니다.", "error");
        }

    } catch (error) {
        console.error('Error:', error);
        showToast("통신 중 오류가 발생했습니다.", "error");
    }

    closeRegisterConfirmModal();
    closeWriteModal();
}

// 6. 삭제 기능
function deleteInquiry(id) {
    deleteTargetId = id;
    document.getElementById('deleteConfirmModal').style.display = 'flex';
}

async function confirmDelete() {
    if (!deleteTargetId) return;

    try {
        const response = await fetch(`/api/qnas/${deleteTargetId}`, {
            method: 'DELETE'
        });

        const result = await response.json();

        if (response.ok && (result.code === 200 || result.success)) {
            showToast("삭제되었습니다.", "info");
            document.getElementById('detail-view').style.display = 'none';
            document.getElementById('empty-detail-view').style.display = 'flex';
            closeMobileModal();

            await loadQnaData(); // 목록 새로고침
        } else {
            showToast(result.message || "삭제에 실패했습니다.", "error");
        }
    } catch (error) {
        console.error('Delete Error:', error);
        showToast("삭제 중 오류가 발생했습니다.", "error");
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