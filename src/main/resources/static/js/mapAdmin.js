// 샘플 데이터 (jobmanage.js와 동일한 구조 사용)
const jobData = [
    { id: 1, title: "웹개발자 모집", company: "ABC테크", lat: 36.3504, lng: 127.3845, address: "대전 서구 둔산동" },
    { id: 2, title: "시니어 디자이너", company: "XYZ디자인", lat: 36.3520, lng: 127.3860, address: "대전 서구 탄방동" },
    { id: 3, title: "마케팅 담당자", company: "MKT그룹", lat: 36.3490, lng: 127.3820, address: "대전 서구 갈마동" },
    { id: 4, title: "시설 관리직", company: "행복시설", lat: 36.3550, lng: 127.3900, address: "대전 서구 월평동" }
];

let map;
let markers = [];
const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

window.onload = function() {
    const jobListBox = document.getElementById('job-list-box');

    kakao.maps.load(function() {
        initMap();
        renderListAndMarkers(jobListBox);
    });
};

function initMap() {
    const container = document.getElementById('map');
    // 기본 위치: 대전 시청
    const defaultCenter = new kakao.maps.LatLng(36.3504, 127.3845);
    const options = {
        center: defaultCenter,
        level: 5
    };
    map = new kakao.maps.Map(container, options);

    // 레이아웃 재조정
    setTimeout(() => {
        if(map) map.relayout();
    }, 300);
}

function renderListAndMarkers(container) {
    container.innerHTML = '';
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
        });

        container.appendChild(div);

        // 2. 지도 마커 생성
        const position = new kakao.maps.LatLng(job.lat, job.lng);
        const marker = new kakao.maps.Marker({
            map: map,
            position: position,
            title: job.title,
            image: markerImage,
            clickable: true
        });

        // 마커 클릭 이벤트
        kakao.maps.event.addListener(marker, 'click', function() {
            panTo(job.lat, job.lng);
            highlightItem(div);
            // 모바일에서는 리스트로 스크롤 이동
            div.scrollIntoView({ behavior: 'smooth', block: 'center' });
        });

        markers.push(marker);
        bounds.extend(position);
    });

    // 모든 마커가 보이도록 지도 범위 재설정
    if (jobData.length > 0) {
        map.setBounds(bounds);
    }
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