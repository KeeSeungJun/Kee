// 일자리 데이터
let jobData = [];
let map;
let markers = [];
let clusterer; // 클러스터러 추가
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
        
        console.log('[DEBUG] API 응답:', data);
        
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
                renderListAndMarkers(container);
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
        level: 13 // 전국이 보이도록 레벨 증가
    };
    map = new kakao.maps.Map(container, options);

    // 마커 클러스터러 생성 (기본 스타일 사용)
    clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 5
    });

    // 레이아웃 재조정
    setTimeout(() => {
        if(map) map.relayout();
    }, 300);
}

function renderListAndMarkers(container) {
    container.innerHTML = '';
    
    // 마커들을 저장할 배열
    const markerList = [];
    const markerImage = new kakao.maps.MarkerImage(imageSrc, new kakao.maps.Size(24, 35));

    // 마커들을 포함할 bounds 생성
    const bounds = new kakao.maps.LatLngBounds();

    jobData.forEach(job => {
        // 1. 리스트 아이템 생성
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

        // 리스트 클릭 시 지도 이동
        div.addEventListener('click', () => {
            panTo(job.lat, job.lng);
            highlightItem(div);
            // 줌 레벨 조정
            map.setLevel(3);
        });

        container.appendChild(div);

        // 2. 지도 마커 생성
        const position = new kakao.maps.LatLng(job.lat, job.lng);
        const marker = new kakao.maps.Marker({
            position: position,
            title: job.title,
            image: markerImage
        });

        // 마커 클릭 이벤트
        kakao.maps.event.addListener(marker, 'click', function() {
            panTo(job.lat, job.lng);
            highlightItem(div);
            map.setLevel(3);
            // 모바일에서는 리스트로 스크롤 이동
            div.scrollIntoView({ behavior: 'smooth', block: 'center' });
        });

        markerList.push(marker);
        markers.push(marker);
        bounds.extend(position);
    });

    // 클러스터러에 마커 추가
    if (clusterer) {
        clusterer.addMarkers(markerList);
    }

    // 모든 마커가 보이도록 지도 범위 재설정
    if (jobData.length > 0) {
        map.setBounds(bounds);
    }
    
    console.log('[INFO] 지도에 표시된 마커 수:', markerList.length);
}

function panTo(lat, lng) {
    const moveLatLon = new kakao.maps.LatLng(lat, lng);
    map.panTo(moveLatLon);
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