// 일자리 데이터
let jobData = [];
let map;
let currentMarker = null; // 현재 표시된 마커를 저장할 변수
const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

window.onload = function() {
    const jobListBox = document.getElementById('job-list-box');

    kakao.maps.load(function() {
        initMap();
        loadJobDataFromDB(jobListBox);
    });
};

// DB에서 일자리 데이터 로드 (위도/경도 있는 것만)
async function loadJobDataFromDB(container) {
    console.log('[DEBUG] 일자리 데이터 로드 시작');

    try {
        const response = await fetch('/api/jobs');
        const data = await response.json();

        if (data.jobs) {
            // 위도/경도가 있는 일자리만 필터링
            jobData = data.jobs
                .filter(job => job.jobLocationLat != null && job.jobLocationLon != null)
                .map(job => ({
                    id: job.jobNo,
                    title: job.jobTitle || '제목 없음',
                    company: job.jobCompanyName || '회사명 없음',
                    lat: parseFloat(job.jobLocationLat),
                    lng: parseFloat(job.jobLocationLon),
                    address: job.jobWorkLocation || '주소 없음'
                }));

            console.log('[SUCCESS] 위치 정보가 있는 일자리:', jobData.length + '개');

            if (jobData.length > 0) {
                renderList(container); // 함수 이름 변경: renderListAndMarkers -> renderList
            } else {
                container.innerHTML = '<div class="no-data">위치 정보가 등록된 일자리가 없습니다.</div>';
            }
        } else {
            console.error('[ERROR] data.jobs가 없습니다');
            container.innerHTML = '<div class="no-data">데이터를 불러올 수 없습니다.</div>';
        }
    } catch (error) {
        console.error('[ERROR] 일자리 데이터 로드 실패:', error);
        container.innerHTML = '<div class="no-data">데이터 로드 중 오류가 발생했습니다.</div>';
    }
}

function initMap() {
    const container = document.getElementById('map');
    // 기본 위치: 대한민국 중심 (세종시)
    const defaultCenter = new kakao.maps.LatLng(36.5, 127.5);
    const options = {
        center: defaultCenter,
        level: 13
    };
    map = new kakao.maps.Map(container, options);

    // [변경] 클러스터러 삭제 (모든 마커를 띄우지 않으므로 불필요)

    // 레이아웃 재조정
    setTimeout(() => {
        if(map) map.relayout();
    }, 300);
}

// [변경] 리스트만 렌더링하고, 클릭 시 마커 표시
function renderList(container) {
    container.innerHTML = '';

    jobData.forEach(job => {
        // 리스트 아이템 생성
        const div = document.createElement('div');
        div.className = 'job-item';
        div.id = `job-item-${job.id}`;

        div.innerHTML = `
            <div class="job-info">
                <strong>${job.title}</strong>
                <span>${job.company}</span>
                <div class="addr"><i class="fa-solid fa-location-dot"></i> ${job.address}</div>
            </div>
        `;

        // 리스트 클릭 이벤트
        div.addEventListener('click', () => {
            // 1. 지도 이동 및 줌 레벨 조정
            const moveLatLon = new kakao.maps.LatLng(job.lat, job.lng);
            map.setLevel(3);
            map.panTo(moveLatLon);

            // 2. 리스트 하이라이트
            highlightItem(div);

            // 3. [핵심] 해당 위치에 단일 마커 표시
            showMarker(job);
        });

        container.appendChild(div);
    });
}

// 단일 마커 표시 함수
function showMarker(job) {
    const position = new kakao.maps.LatLng(job.lat, job.lng);
    const markerImage = new kakao.maps.MarkerImage(imageSrc, new kakao.maps.Size(24, 35));

    // 기존 마커가 있다면 제거
    if (currentMarker) {
        currentMarker.setMap(null);
    }

    // 새 마커 생성 및 지도에 표시
    currentMarker = new kakao.maps.Marker({
        position: position,
        title: job.title,
        image: markerImage,
        map: map // 생성과 동시에 지도에 표시
    });
}

function highlightItem(element) {
    // 모든 아이템 active 제거
    document.querySelectorAll('.job-item').forEach(item => item.classList.remove('active'));
    // 선택된 아이템 active 추가
    element.classList.add('active');
}

// 화면 크기 변경 시 지도 재조정
window.addEventListener('resize', function() {
    if (map) {
        map.relayout();
    }
});