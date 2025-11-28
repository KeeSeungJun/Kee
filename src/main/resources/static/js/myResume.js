// [하드코딩] 임시 데이터 (서버 연동 전 테스트용)
const myApplications = [
    {
        id: 1,
        jobTitle: "승합차 운전원",
        company: "큰마을아파트 관리사무소",
        date: "2024.05.20",
        status: "waiting", // waiting, pass, fail
        adminReply: null // 답변 없음
    },
    {
        id: 2,
        jobTitle: "환경미화원",
        company: "대덕복지센터",
        date: "2024.05.18",
        status: "pass",
        adminReply: "서류 합격하셨습니다. 추후 면접 장소 및 일자를 문자 보내드릴 예정입니다."
    },
    {
        id: 3,
        jobTitle: "요양보호사",
        company: "효사랑 요양원",
        date: "2024.05.10",
        status: "fail",
        adminReply: "아쉽게도 이번 채용은 마감되었습니다. 지원해주셔서 감사합니다."
    }
];

document.addEventListener('DOMContentLoaded', () => {
    renderApplyList();
});

function renderApplyList() {
    const container = document.getElementById('applyListContainer');
    container.innerHTML = '';

    if (myApplications.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="fa-regular fa-folder-open"></i>
                <p>아직 지원한 내역이 없습니다.</p>
            </div>
        `;
        return;
    }

    // 최신순 정렬 (ID 역순)
    myApplications.sort((a, b) => b.id - a.id);

    myApplications.forEach(item => {
        const card = document.createElement('div');
        card.className = 'apply-card';

        // 상태별 텍스트 및 클래스 설정
        let statusText = '대기중';
        let statusClass = 'status-waiting';

        if (item.status === 'pass') {
            statusText = '합격 / 면접제안';
            statusClass = 'status-pass';
        } else if (item.status === 'fail') {
            statusText = '불합격';
            statusClass = 'status-fail';
        }

        // 답변이 있는 경우 HTML 생성
        let replyHtml = '';
        if (item.adminReply) {
            replyHtml = `
                <div class="admin-reply-box">
                    <span class="reply-label"><i class="fa-solid fa-comment-dots"></i> 담당자 답변</span>
                    ${item.adminReply}
                </div>
            `;
        } else {
            replyHtml = `
                <div style="font-size:13px; color:#ccc; margin-top:10px;">
                    아직 담당자의 답변이 없습니다.
                </div>
            `;
        }

        card.innerHTML = `
            <div class="card-header">
                <div class="job-info">
                    <h3>${item.jobTitle}</h3>
                    <span class="company">${item.company}</span>
                </div>
                <span class="status-badge ${statusClass}">${statusText}</span>
            </div>
            <div class="card-body">
                지원일 : ${item.date}
            </div>
            ${replyHtml}
        `;

        container.appendChild(card);
    });
}