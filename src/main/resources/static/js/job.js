const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

let modal;

let map;

// var container = document.getElementById('map');
//
// // 목원대 D관 위치 설정 (지도 기본 위치값은 목원대 d관입니다)
// var options = {
//     center: new kakao.maps.LatLng(36.32183, 127.3386),
//     level: 3
// };
// var map = new kakao.maps.Map(container, options);

function getRandomScore(min = 70, max = 100) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

async function handleCurrentMap() {
    const container = document.getElementById('map');
    const pos = await getCurrentPosition();
    const options = {
        center: new kakao.maps.LatLng(pos.latitude, pos.longitude),
        level: 12
    };
    map = new kakao.maps.Map(container, options);
}

async function handleGet(jobListBox) {;
    const jobItems = Array.from(jobListBox.getElementsByClassName('job-item'));
    modal = document.getElementById('job-modal');

    const markerImage = new kakao.maps.MarkerImage(imageSrc, new kakao.maps.Size(24, 35));

    showLoading();
    try {
        const jobData = await AjaxUtils.get('http://localhost:8080/api/job');
        // resultDiv.textContent = JSON.stringify(data, null, 2);
        console.log(jobData.list);

        jobItems.forEach((item, index) => {
            // const score = getRandomScore();
            // const scoreSpan = document.getElementById('score' + (index + 1));
            // if (scoreSpan) {
            //     scoreSpan.innerText = score;
            // }
            // item.dataset.score = score;
            console.log(JSON.stringify(jobData.list[index], null, 2));

            item.addEventListener('click', function () {
                const data = jobData.list[index];
                document.getElementById('modal-title').innerText = data.job_task + " 상세정보";
                document.getElementById('job-name').innerText = data.job_title;
                document.getElementById('salary').innerText = data.job_salary;
                document.getElementById('location').innerText = data.job_address;
                document.getElementById('company').innerText = data.job_desc;
                document.getElementById('nearby').innerText = data.job_nearby_subway;

                document.getElementById('modal-score').innerText = `추천 점수 : ${data.score}/100`;

                modal.style.display = 'flex';

                const modalMap = document.getElementById('modal-map');
                modalMap.innerHTML = '';
                const modalMapInstance = new kakao.maps.Map(modalMap, {
                    center: new kakao.maps.LatLng(data.job_latitude, data.job_longitude),
                    level: 3
                });
            });
        });

        // 지도에 마커 추가
        for (var i = 0; i < jobData.list.length; i ++) {
            // 마커를 생성합니다
            var marker = new kakao.maps.Marker({
                map: map, // 마커를 표시할 지도
                position:new kakao.maps.LatLng(jobData.list[i].job_latitude, jobData.list[i].job_longitude), // 마커를 표시할 위치
                title : jobData.list[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                image : markerImage // 마커 이미지
            });
        }

        jobItems.sort((a, b) => b.dataset.score - a.dataset.score);
        jobListBox.innerHTML = '';
        jobItems.forEach(item => jobListBox.appendChild(item));

        window.addEventListener('click', function (e) {
            if (e.target === modal) {
                closeModal();
            }
        });

    } catch (error) {
        // resultDiv.textContent = `에러 발생: ${error.message}`;
        console.log(error);
    } finally {
        console.log('finally');
        hideLoading();
    }
}

window.onload = function () {
    const jobListBox = document.getElementById('job-list-box')
    handleCurrentMap();
    handleGet(jobListBox);

    // // 예시 데이터@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // const jobData = [
    //     {name: "청소 업무", salary: "220만원", location: "대전 서구", company: "클린업", contact: "042-123-4567"},
    //     {name: "배달 보조", salary: "200만원", location: "대전 중구", company: "퀵익스프레스", contact: "010-2222-3333"},
    //     {name: "식당 서빙", salary: "230만원", location: "대전 유성구", company: "맛집식당", contact: "042-987-6543"},
    // ];
    //
    // jobItems.forEach((item, index) => {
    //     const score = getRandomScore();
    //     const scoreSpan = document.getElementById('score' + (index + 1));
    //     if (scoreSpan) {
    //         scoreSpan.innerText = score;
    //     }
    //     item.dataset.score = score;
    //
    //     item.addEventListener('click', function () {
    //         const data = jobData[index];
    //         document.getElementById('modal-title').innerText = data.company + " 상세정보";
    //         document.getElementById('job-name').innerText = data.name;
    //         document.getElementById('salary').innerText = data.salary;
    //         document.getElementById('location').innerText = data.location;
    //         document.getElementById('company').innerText = data.company;
    //         document.getElementById('contact').innerText = data.contact;
    //
    //         document.getElementById('modal-score').innerText = `추천 점수 : ${score}/100`;
    //
    //         modal.style.display = 'flex';
    //
    //         const modalMap = document.getElementById('modal-map');
    //         modalMap.innerHTML = '';
    //         const modalMapInstance = new kakao.maps.Map(modalMap, {
    //             center: new kakao.maps.LatLng(37.49153310972793, 127.07426887279011),
    //             level: 3
    //         });
    //     });
    // });

    // jobItems.sort((a, b) => b.dataset.score - a.dataset.score);
    // jobListBox.innerHTML = '';
    // jobItems.forEach(item => jobListBox.appendChild(item));
    //
    // window.addEventListener('click', function (e) {
    //     if (e.target === modal) {
    //         closeModal();
    //     }
    // });
};

function closeModal() {
    document.getElementById('job-modal').style.display = 'none';
}

document.addEventListener('DOMContentLoaded', function () {
    const applyButtons = document.querySelectorAll('.apply-btn');
    applyButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            alert('신청이 완료되었습니다!');
        });
    });
});