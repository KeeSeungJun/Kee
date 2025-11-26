// ==========================================
// 페이지 로드 시 저장된 폰트 크기(Zoom), (모든 페이지가 열릴 때마다 자동으로 실행)
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
    const savedSize = localStorage.getItem("userFontSizePx");

    if (savedSize) {
        document.documentElement.style.setProperty('--main-font-size', savedSize + "px");
    }
});

// ==========================================
// 로딩 인디케이터 관련 함수
// ==========================================
function showLoading() {
    const indicator = document.getElementById('loading-indicator');
    if (indicator) {
        indicator.style.display = 'flex';
    }
}

function hideLoading() {
    const indicator = document.getElementById('loading-indicator');
    if (indicator) {
        indicator.style.display = 'none';
    }
}

// ==========================================
// 위치 정보 관련 함수
// ==========================================
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