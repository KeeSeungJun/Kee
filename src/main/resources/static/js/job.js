// <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=a54b9ee8fe4d9a9b2ba99d007d423f0b"></script>
    var container = document.getElementById('map');

    // 목원대 D관 위치 설정 (지도 기본 위치값은 목원대 d관입니다)
    var options = {
    center: new kakao.maps.LatLng(36.32183, 127.3386),
    level: 3
};
    var map = new kakao.maps.Map(container, options);

    function getRandomScore(min = 70, max = 100) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

    let modal;

    window.onload = function () {
    const jobListBox = document.getElementById('job-list-box');
    const jobItems = Array.from(jobListBox.getElementsByClassName('job-item'));
    modal = document.getElementById('job-modal');

    // 예시 데이터@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    const jobData = [
{ name: "청소 업무", salary: "220만원", location: "대전 서구", company: "클린업", contact: "042-123-4567" },
{ name: "배달 보조", salary: "200만원", location: "대전 중구", company: "퀵익스프레스", contact: "010-2222-3333" },
{ name: "식당 서빙", salary: "230만원", location: "대전 유성구", company: "맛집식당", contact: "042-987-6543" },
    ];

    jobItems.forEach((item, index) => {
    const score = getRandomScore();
    const scoreSpan = document.getElementById('score' + (index + 1));
    if (scoreSpan) {
    scoreSpan.innerText = score;
}
    item.dataset.score = score;

    item.addEventListener('click', function () {
    const data = jobData[index];
    document.getElementById('modal-title').innerText = data.company + " 상세정보";
    document.getElementById('job-name').innerText = data.name;
    document.getElementById('salary').innerText = data.salary;
    document.getElementById('location').innerText = data.location;
    document.getElementById('company').innerText = data.company;
    document.getElementById('contact').innerText = data.contact;

    document.getElementById('modal-score').innerText = `추천 점수 : ${score}/100`;

    modal.style.display = 'flex';

    const modalMap = document.getElementById('modal-map');
    modalMap.innerHTML = '';
    const modalMapInstance = new kakao.maps.Map(modalMap, {
    center: new kakao.maps.LatLng(37.49153310972793, 127.07426887279011),
    level: 3
});
});
});

    jobItems.sort((a, b) => b.dataset.score - a.dataset.score);
    jobListBox.innerHTML = '';
    jobItems.forEach(item => jobListBox.appendChild(item));

    window.addEventListener('click', function (e) {
    if (e.target === modal) {
    closeModal();
}
});
};

    function closeModal() {
    document.getElementById('job-modal').style.display = 'none';
}

    document.addEventListener('DOMContentLoaded', function () {
    const applyButtons = document.querySelectorAll('.apply-btn');
    applyButtons.forEach(function(button) {
    button.addEventListener('click', function () {
    alert('신청이 완료되었습니다!');
});
});
});