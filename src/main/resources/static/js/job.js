const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

let modal;
let map;

/**
 * 점수에 따라 뱃지 색상을 결정하는 함수
 */
function getScoreClass(score) {
    // 90점 이상: High (Green), 70점 이상: Medium (Yellow), 그 외: Low (Red)
    if (score >= 90) {
        return 'high';
    } else if (score >= 70) {
        return 'medium';
    } else {
        return 'low';
    }
}

/**
 * Modal을 닫는 함수
 */
function closeModal() {
    document.getElementById('job-modal').style.display = 'none';
}

async function handleCurrentMap() {
    const container = document.getElementById('map');
    try {
        const pos = await getCurrentPosition(); // globals.js에 정의된 함수라고 가정
        const center = new kakao.maps.LatLng(pos.latitude, pos.longitude);
        const options = {
            center: center,
            level: 7
        };
        map = new kakao.maps.Map(container, options);
        new kakao.maps.Marker({
            map: map,
            position: center,
            title: '현재 위치'
        });
    } catch (err) {
        console.error("현재 위치를 가져올 수 없습니다.", err);
        // 필요 시 기본 위치(예: 시청)로 지도 생성 로직 추가
    }
}

async function handleGet(jobListBox) {
    const modal = document.getElementById('job-modal');
    const markerImage = new kakao.maps.MarkerImage(imageSrc, new kakao.maps.Size(24, 35));

    function showJobModal(data) {
        // 1. 상세 정보 텍스트 바인딩
        document.getElementById('modal-title').innerText  = `${data.job_task} 상세정보`;
        document.getElementById('job-name').innerText     = data.job_title;
        document.getElementById('salary').innerText       = data.job_salary;
        document.getElementById('location').innerText     = data.job_address;
        document.getElementById('company').innerText      = data.job_desc;
        document.getElementById('nearby').innerText       = data.job_nearby_subway;

        // 2. 추천 점수 설정 (클릭 이벤트 제거 및 커서 스타일 변경)
        const scoreEl = document.getElementById('modal-score');
        scoreEl.innerText = `추천 점수 : ${data.score}/100`;
        scoreEl.style.cursor = 'default';
        scoreEl.onclick = null; // 기존 클릭 이벤트가 있다면 제거

        // 3. [🤖 AI 추천 분석] 박스 내용 채우기
        const aiListEl = document.getElementById('ai-reason-list');
        if (aiListEl) {
            aiListEl.innerHTML = ''; // 기존 내용 초기화

            const reasonKeys = [
                'reason_working',
                'reason_salary',
                'reason_disease',
                'reason_occupation',
                'reason_addr'
            ];

            reasonKeys.forEach(key => {
                if (data[key]) {
                    const li = document.createElement('li');
                    li.innerText = data[key];
                    aiListEl.appendChild(li);
                }
            });
        }

        // 4. 모달 열기
        modal.style.display = 'flex';

        // 5. 모달 내부 지도 생성
        const modalMap = document.getElementById('modal-map');
        modalMap.innerHTML = ''; // 지도 초기화
        const modalMapInstance = new kakao.maps.Map(modalMap, {
            center: new kakao.maps.LatLng(data.job_latitude, data.job_longitude),
            level: 3
        });

        new kakao.maps.Marker({
            map: modalMapInstance,
            position: new kakao.maps.LatLng(data.job_latitude, data.job_longitude),
            title: data.job_title,
            image: markerImage
        });
    }

    showLoading(); // 로딩 시작
    try {
        // API 호출 (상대 경로 권장)
        const { list: jobs } = await AjaxUtils.get('/api/job');

        // 점수 내림차순 정렬
        jobs.sort((a, b) => b.score - a.score);

        jobListBox.innerHTML = '';

        jobs.forEach((data) => {
            const item = document.createElement('div');
            item.className = 'job-item';

            // --- 리스트 아이템(카드) 구조 생성 ---
            const jobInfo = document.createElement('div');
            jobInfo.className = 'job-title-task';
            jobInfo.innerHTML = `
                <strong>${data.job_title}</strong>
                <span>${data.job_address} (${data.job_task})</span>
            `;

            const scoreBadge = document.createElement('div');
            scoreBadge.className = `score-badge ${getScoreClass(data.score)}`;
            scoreBadge.innerText = data.score;

            item.appendChild(jobInfo);
            item.appendChild(scoreBadge);
            // -------------------------------------

            // 클릭 시 모달 열기
            item.addEventListener('click', () => showJobModal(data));
            jobListBox.appendChild(item);

            // 지도 마커 생성
            if (map) {
                const marker = new kakao.maps.Marker({
                    map: map,
                    position: new kakao.maps.LatLng(data.job_latitude, data.job_longitude),
                    title: data.job_title,
                    image: markerImage,
                    clickable: true
                });
                // 마커 클릭 시에도 모달 열기
                kakao.maps.event.addListener(marker, 'click', () => showJobModal(data));
            }
        });

        // 모달 외부 클릭 시 닫기
        window.addEventListener('click', e => {
            if (e.target === modal) closeModal();
        });

    } catch (err) {
        console.error("일자리 목록 조회 실패:", err);
    } finally {
        hideLoading(); // 로딩 종료
    }
}

window.onload = function () {
    const jobListBox = document.getElementById('job-list-box');
    handleCurrentMap();
    handleGet(jobListBox);
};

document.addEventListener('DOMContentLoaded', function () {
    const applyButtons = document.querySelectorAll('.apply-btn');
    applyButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            alert('신청이 완료되었습니다!');
        });
    });
});