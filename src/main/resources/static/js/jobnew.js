// <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=388f60e5d406e9da08ab57134ab2dc46"></script>
var container = document.getElementById('map');

// 지도 기본 위치 (목원대 D관)
var options = {
    center: new kakao.maps.LatLng(36.32183, 127.3386),
    level: 3
};
var map = new kakao.maps.Map(container, options);

// 예시 데이터 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
const jobs = [
    { id: 1, date: "25 05/19", location: "대전 유성구", work: "서빙", salary: "시급 10,000원", company: "유성구 카페", contact: "010-1234-5678"},
    { id: 2, date: "25 05/18", location: "대전 서구", work: "주차 안내", salary: "일급 50,000원", company: "서구 주차장", contact: "010-2345-6789"},
    { id: 3, date: "25 05/17", location: "대전 동구", work: "설거지", salary: "시급 9,000원", company: "동구 식당", contact: "010-3456-7890"},
    { id: 4, date: "25 05/16", location: "대전 중구", work: "물류 정리", salary: "시급 9,500원", company: "중구 물류센터", contact: "010-4567-8901"},
    { id: 5, date: "25 05/15", location: "대전 대덕구", work: "행사진행", salary: "시급 11,000원", company: "대덕 행사대행", contact: "010-5678-9012"},
    { id: 6, date: "25 05/14", location: "대전 유성구", work: "카운터", salary: "시급 10,500원", company: "유성구 편의점", contact: "010-6789-0123"}
];

const jobList = document.getElementById('jobList');
const modal = document.getElementById('job-modal');

// 일자리 목록 생성 및 클릭 이벤트 추가
jobs.forEach(job => {
    const card = document.createElement('div');
    card.className = 'job-card';
    card.innerHTML = `
        <div class="job-info date">${job.date}</div>
        <div class="job-info location">${job.location}</div>
        <div class="job-info work">${job.work}</div>
    `;

    card.addEventListener('click', () => {
        // 모달 텍스트 정보 업데이트
        document.getElementById('modal-title').innerText = job.company + " 상세정보";
        document.getElementById('job-name').innerText = job.work;
        document.getElementById('salary').innerText = job.salary;
        document.getElementById('location').innerText = job.location;
        document.getElementById('company').innerText = job.company;
        document.getElementById('contact').innerText = job.contact;

        modal.style.display = 'flex';

        // 목대 위치로 지도 펼침
        const modalMap = document.getElementById('modal-map');
        modalMap.innerHTML = '';

        const modalMapInstance = new kakao.maps.Map(modalMap, {
            center: new kakao.maps.LatLng(36.32183, 127.3386),
            level: 3
        });
    });

    jobList.appendChild(card);
});

window.addEventListener('click', function(e) {
    if (e.target === modal) {
        modal.style.display = 'none';
    }
});

document.addEventListener('click', function(e) {
    if (e.target.classList.contains('apply-btn')) {
        alert('신청이 완료되었습니다!');
    }
});