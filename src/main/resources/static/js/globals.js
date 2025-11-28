// ==========================================
// 페이지 로드 시 저장된 폰트 크기(Zoom) 적용
// 기본값(Base): 20px (1rem = 20px)
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
    applySavedFontSize();
});

function applySavedFontSize() {
    // 저장된 설정이 없으면 기본값 20(px) 사용
    const savedSize = localStorage.getItem("userFontSize");
    const fontSize = savedSize ? parseInt(savedSize) : 20;

    // HTML 루트 요소의 폰트 사이즈 변경 -> rem 단위의 기준값 변경 효과
    // 이 값이 변하면 rem을 사용하는 모든 폰트 크기가 비율대로 변함
    document.documentElement.style.fontSize = fontSize + "px";
}

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