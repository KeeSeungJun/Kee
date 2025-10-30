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
 * Modal을 닫는 함수 (전역 사용 가능함)
 */
function closeModal() {
    document.getElementById('job-modal').style.display = 'none';
}
function closeReasonModal() {
    document.getElementById('reason-modal').style.display = 'none';
}


async function handleCurrentMap() {
    const container = document.getElementById('map');
    const pos = await getCurrentPosition();
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
}

async function handleGet(jobListBox) {
    const modal       = document.getElementById('job-modal');
    const reasonModal = document.getElementById('reason-modal');
    const markerImage = new kakao.maps.MarkerImage(imageSrc, new kakao.maps.Size(24, 35));

    function showJobModal(data) {
        document.getElementById('modal-title').innerText  = `${data.job_task} 상세정보`;
        document.getElementById('job-name').innerText     = data.job_title;
        document.getElementById('salary').innerText       = data.job_salary;
        document.getElementById('location').innerText     = data.job_address;
        document.getElementById('company').innerText      = data.job_desc;
        document.getElementById('nearby').innerText       = data.job_nearby_subway;
        const scoreEl = document.getElementById('modal-score');
        scoreEl.innerText = `추천 점수 : ${data.score}/100`;
        scoreEl.onclick = () => showReasonModal(data);


        modal.style.display = 'flex';

        const modalMap = document.getElementById('modal-map');
        modalMap.innerHTML = '';
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

    showLoading();
    try {
        const { list: jobs } = await AjaxUtils.get('http://localhost:8080/api/job');

        jobs.sort((a, b) => b.score - a.score);

        jobListBox.innerHTML = '';

        jobs.forEach((data, idx) => {
            const item = document.createElement('div');
            item.className = 'job-item';

            // --- START: New HTML structure for modern card design ---
            const jobInfo = document.createElement('div');
            jobInfo.className = 'job-title-task';

            // Display Job Title and truncated Location/Task in the card
            jobInfo.innerHTML = `
                <strong>${data.job_title}</strong>
                <span>${data.job_address} (${data.job_task})</span>
            `;

            // New Score Badge with dynamic class
            const scoreBadge = document.createElement('div');
            scoreBadge.className = `score-badge ${getScoreClass(data.score)}`;
            scoreBadge.innerText = data.score;

            item.appendChild(jobInfo);
            item.appendChild(scoreBadge);
            // --- END: New HTML structure ---

            item.addEventListener('click', () => showJobModal(data));
            jobListBox.appendChild(item);

            const marker = new kakao.maps.Marker({
                map: map,
                position: new kakao.maps.LatLng(data.job_latitude, data.job_longitude),
                title: data.job_title,
                image: markerImage,
                clickable: true
            });
            kakao.maps.event.addListener(marker, 'click', () => showJobModal(data));
        });

        // 모달 닫기 이벤트 리스너 보강
        window.addEventListener('click', e => {
            if (e.target === modal) closeModal();
            if (e.target === reasonModal) closeReasonModal();
        });

    } catch (err) {
        console.error(err);
    } finally {
        hideLoading();
    }
}


window.onload = function () {
    const jobListBox = document.getElementById('job-list-box')
    handleCurrentMap();
    handleGet(jobListBox);
};

const reasonIcons = [
    'fa-regular fa-clock',
    'fa-money-bill',
    'fa-heart-pulse',
    'fa-briefcase',
    'fa-route'
];

function showReasonModal(data) {
    const listEl = document.getElementById('score-reason-list');
    listEl.innerHTML = '';

    const keys = [
        'reason_working',
        'reason_salary',
        'reason_disease',
        'reason_occupation',
        'reason_addr'
    ];

    keys.forEach((key, idx) => {
        const text = data[key];
        if (text) {
            const li = document.createElement('li');
            li.innerHTML = `
                <i class="fa-solid ${reasonIcons[idx]}"></i>
                ${text}
              `;
            listEl.appendChild(li);
        }
    });

    document.getElementById('reason-modal').style.display = 'flex';
}


document.addEventListener('DOMContentLoaded', function () {
    const applyButtons = document.querySelectorAll('.apply-btn');
    applyButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            alert('신청이 완료되었습니다!');
        });
    });
});