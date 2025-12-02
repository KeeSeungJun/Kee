const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

let modal;
let map; // 메인 지도 객체

function getScoreClass(score) {
    if (score >= 90) return 'high';
    else if (score >= 70) return 'medium';
    else return 'low';
}

// 모달 닫기
function closeModal() {
    document.getElementById('job-modal').style.display = 'none';
    document.body.classList.remove('no-scroll');
}

// 1. 메인 지도 생성 (대전 시청 중심 고정)
function handleCurrentMap() {
    const container = document.getElementById('map');

    // 대전 시청 좌표 고정
    const defaultCenter = new kakao.maps.LatLng(36.3504, 127.3845);
    const options = {
        center: defaultCenter,
        level: 8,
        draggable: true,  // [수정] 드래그 가능 명시
        scrollwheel: true // [수정] 스크롤 줌 가능
    };

    // 지도 생성
    map = new kakao.maps.Map(container, options);

    // [핵심 수정] 모바일 호환성을 위해 한번 더 강제 활성화
    map.setDraggable(true);
    map.setZoomable(true);

    // [중요] 레이아웃 깨짐 방지 (흰 화면 방지)
    setTimeout(() => {
        if(map) {
            map.relayout();
            map.setCenter(defaultCenter);
        }
    }, 500);
}

// 2. 리스트 데이터 조회 및 마커 생성
async function handleGet(jobListBox) {
    const modal = document.getElementById('job-modal');
    // 별 모양 마커 이미지
    const markerImage = new kakao.maps.MarkerImage(imageSrc, new kakao.maps.Size(24, 35));

    // 상세 모달 띄우기 함수 (리스트, 마커 공용)
    function showJobModal(data) {
        document.getElementById('modal-title').innerText  = `${data.job_task} 상세정보`;
        document.getElementById('job-name').innerText     = data.job_title;
        document.getElementById('salary').innerText       = data.job_salary;
        document.getElementById('location').innerText     = data.job_address;
        document.getElementById('company').innerText      = data.job_desc;
        document.getElementById('nearby').innerText       = data.job_nearby_subway;
        document.getElementById('modal-score').innerText = `추천 점수 : ${data.score}/100`;

        const aiListEl = document.getElementById('ai-reason-list');
        if (aiListEl) {
            aiListEl.innerHTML = '';
            const reasonKeys = ['reason_working', 'reason_salary', 'reason_disease', 'reason_occupation', 'reason_addr'];
            reasonKeys.forEach(key => {
                if (data[key]) {
                    const li = document.createElement('li');
                    li.innerText = data[key];
                    aiListEl.appendChild(li);
                }
            });
        }

        modal.style.display = 'flex';
        document.body.classList.add('no-scroll');

        // 모달 내부 지도 생성
        setTimeout(() => {
            const modalMapDiv = document.getElementById('modal-map');
            modalMapDiv.innerHTML = '';

            // 좌표 데이터 숫자 변환
            const lat = parseFloat(data.job_latitude);
            const lng = parseFloat(data.job_longitude);
            const position = new kakao.maps.LatLng(lat, lng);

            const modalMapInstance = new kakao.maps.Map(modalMapDiv, {
                center: position,
                level: 3,
                draggable: true // [수정] 모달 지도도 드래그 가능하게
            });
            new kakao.maps.Marker({
                map: modalMapInstance,
                position: position,
                title: data.job_title,
                image: markerImage
            });
            modalMapInstance.relayout();
            modalMapInstance.setCenter(position);
        }, 200);
    }

    showLoading();
    try {
        const response = await AjaxUtils.get('/api/job');
        const jobs = response.list;
        jobs.sort((a, b) => b.score - a.score);
        jobListBox.innerHTML = '';

        // [데이터 루프 시작]
        jobs.forEach((data) => {
            // --- A. 리스트 아이템 생성 ---
            const item = document.createElement('div');
            item.className = 'job-item';

            const jobInfo = document.createElement('div');
            jobInfo.className = 'job-title-task';
            // job_task(직종) + job_desc(기업명)을 조합하여 표시
            const jobDesc = data.job_desc && data.job_desc !== '-' ? data.job_desc : '';
            const jobTask = data.job_task || '';
            const displayTitle = jobDesc ? `[${jobDesc}] ${jobTask}` : jobTask;
            jobInfo.innerHTML = `<strong>${displayTitle}</strong><span>${data.job_address || ''}</span>`;

            const scoreBadge = document.createElement('div');
            scoreBadge.className = `score-badge ${getScoreClass(data.score)}`;
            scoreBadge.innerText = data.score;

            item.appendChild(jobInfo);
            item.appendChild(scoreBadge);

            // 리스트 클릭 이벤트 -> 모달 열기
            item.addEventListener('click', () => showJobModal(data));
            jobListBox.appendChild(item);

            // --- B. 메인 지도 마커 생성 (여기가 추가된 부분) ---
            if (map && data.job_latitude && data.job_longitude) {
                // 문자열로 올 수 있는 좌표를 숫자로 안전하게 변환
                const lat = parseFloat(data.job_latitude);
                const lng = parseFloat(data.job_longitude);

                // 좌표가 정상적인 숫자인 경우에만 마커 생성
                if (!isNaN(lat) && !isNaN(lng)) {
                    const markerPosition = new kakao.maps.LatLng(lat, lng);

                    const marker = new kakao.maps.Marker({
                        map: map,               // 전역 map 객체에 표시
                        position: markerPosition,
                        title: data.job_title,  // 마우스 오버 시 제목
                        image: markerImage,     // 별 이미지
                        clickable: true         // 클릭 가능 여부
                    });

                    // [핵심] 마커 클릭 이벤트 -> 모달 열기 (리스트 클릭과 동일한 동작)
                    kakao.maps.event.addListener(marker, 'click', function() {
                        showJobModal(data);
                    });
                }
            }
        });

        window.addEventListener('click', e => {
            if (e.target === modal) closeModal();
        });

    } catch (err) {
        console.error("일자리 목록 조회 실패:", err);
    } finally {
        hideLoading();
    }
}

// [필수] 실행 순서 보장
window.onload = function () {
    const jobListBox = document.getElementById('job-list-box');

    kakao.maps.load(function() {
        // 1. 지도 먼저 생성 (대전 시청)
        handleCurrentMap();
        // 2. 리스트 데이터 가져오기 + 마커 찍기
        handleGet(jobListBox);
    });
};

// 화면 크기 변경 시 지도 재조정
window.addEventListener('resize', function() {
    if (map) {
        map.relayout();
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const applyButtons = document.querySelectorAll('.apply-btn');
    applyButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            showToast('신청이 완료되었습니다!', 'success');
            // setTimeout(closeModal, 1000);
        });
    });
});