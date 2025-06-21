function showLoading() {
    document.getElementById('loading-indicator').style.display = 'flex';
}

function hideLoading() {
    document.getElementById('loading-indicator').style.display = 'none';
}

// Promise로 위도, 경도 값을 리턴하는 함수
function getCurrentPosition() {
    return new Promise(function(resolve, reject) {
        if (!navigator.geolocation) {
            reject(new Error('이 브라우저에서는 위치 정보가 지원되지 않습니다.'));
        } else {
            navigator.geolocation.getCurrentPosition(
                function(position) {
                    resolve({
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude
                    });
                },
                function(error) {
                    reject(error);
                }
            );
        }
    });
}
