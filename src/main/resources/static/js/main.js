let tempFontSize = 20; // 모달 내에서 조절 중인 임시 값 (기본 20)

function openFontModal() {
    const modal = document.getElementById('font-modal');

    // 현재 적용된 폰트 사이즈 가져오기 (없으면 기본 20)
    const saved = localStorage.getItem("userFontSize");
    tempFontSize = saved ? parseInt(saved) : 20;

    updatePreviewUI();
    modal.style.display = 'flex';
}

function closeFontModal() {
    // 취소 시 아무것도 적용하지 않고 닫기만 함 (뒷 배경은 원래 상태 유지됨)
    document.getElementById('font-modal').style.display = 'none';
}

function adjustFontSize(change) {
    const newSize = tempFontSize + change;

    // 범위 제한: 15px ~ 25px
    if (newSize >= 15 && newSize <= 25) {
        tempFontSize = newSize;
        updatePreviewUI();
    }
}

function updatePreviewUI() {
    // 1. 현재 설정된 픽셀 값 표시
    document.getElementById('current-zoom-level').innerText = tempFontSize + "px";

    // 2. [핵심] 미리보기 텍스트만 크기 변경 (뒷 배경 영향 X)
    // 미리보기 박스 내 텍스트에 직접 font-size를 지정하여 시뮬레이션
    const previewText = document.getElementById('font-preview-text');
    if (previewText) {
        // 1rem이 tempFontSize 만큼 커졌을 때를 가정하여 적용
        previewText.style.fontSize = tempFontSize + "px";
        previewText.style.lineHeight = "1.5"; // 줄간격 유지
    }
}

function saveFontSize() {
    // [저장] 로컬 스토리지에 값 저장
    localStorage.setItem("userFontSize", tempFontSize);

    // [적용] 실제 화면(Root)의 폰트 사이즈 변경 -> 전체 페이지 반영
    document.documentElement.style.fontSize = tempFontSize + "px";

    showToast("글자 크기가 변경되었습니다.", "success");
    document.getElementById('font-modal').style.display = 'none';
}