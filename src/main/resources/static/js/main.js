let currentFontSizePx = 28;

function openFontModal() {
    const modal = document.getElementById('font-modal');
    const savedSize = localStorage.getItem("userFontSizePx");
    currentFontSizePx = savedSize ? parseInt(savedSize) : 28;

    updatePreviewUI();
    modal.style.display = 'flex';
}

function closeFontModal() {
    document.getElementById('font-modal').style.display = 'none';
}

function adjustFontSize(change) {
    const newSize = currentFontSizePx + change;

    if (newSize >= 20 && newSize <= 40) {
        currentFontSizePx = newSize;
        updatePreviewUI();
    }
}

function updatePreviewUI() {
    document.getElementById('current-zoom-level').innerText = currentFontSizePx + "px";

    const previewText = document.getElementById('font-preview-text');
    if (previewText) {
        previewText.style.fontSize = currentFontSizePx + "px";
    }
}

function saveFontSize() {
    localStorage.setItem("userFontSizePx", currentFontSizePx);
    document.documentElement.style.setProperty('--main-font-size', currentFontSizePx + "px");

    showToast("글자 크기가 변경되었습니다.", "success");

    closeFontModal();
}