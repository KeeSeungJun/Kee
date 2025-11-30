/*
 * 고용24 직업분류 데이터 추출 스크립트
 * 
 * 사용 방법:
 * 1. https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do 접속
 * 2. F12 개발자 도구 열기
 * 3. Console 탭에서 이 스크립트 전체 복사 후 실행
 * 4. 결과를 복사하여 파일로 저장
 */

async function extractJobCategories() {
    const result = {};
    
    console.log("=" + "=".repeat(60));
    console.log("고용24 직업분류 데이터 추출 시작");
    console.log("=" + "=".repeat(60));
    
    // 1차 분류 추출
    console.log("\n1차 분류 수집 중...");
    for (let i = 1; i <= 13; i++) {
        const btnId = `btnjobName${String(i).padStart(2, '0')}`;
        const btn = document.getElementById(btnId);
        
        if (btn) {
            const name = btn.textContent.trim();
            const code = btn.value || '';
            
            console.log(`  [${btnId}] ${name} (코드: ${code})`);
            
            result[btnId] = {
                name: name,
                code: code,
                children: {}
            };
            
            // 버튼 클릭하여 2차 분류 표시
            btn.click();
            await new Promise(resolve => setTimeout(resolve, 500));
            
            // 2차 분류 추출
            console.log(`    2차 분류 수집 중...`);
            
            // 모든 버튼 중 btnjobName으로 시작하고 현재 1차 코드를 포함하는 것 찾기
            const allButtons = document.querySelectorAll('button[id^="btnjobName"]');
            
            for (const secBtn of allButtons) {
                const secId = secBtn.id;
                
                // 1차 버튼 자신은 제외, 1차 코드로 시작하는 2차 버튼만 포함
                if (secId !== btnId && secId.startsWith(btnId.substring(0, 12))) {
                    const secName = secBtn.textContent.trim();
                    const secCode = secBtn.value || '';
                    
                    if (secName) {
                        console.log(`      [${secId}] ${secName} (코드: ${secCode})`);
                        
                        result[btnId].children[secId] = {
                            name: secName,
                            code: secCode,
                            children: []
                        };
                        
                        // 3차 분류 추출
                        secBtn.click();
                        await new Promise(resolve => setTimeout(resolve, 300));
                        
                        // 3차 분류 체크박스 찾기
                        const checkboxes = document.querySelectorAll('input[type="checkbox"]');
                        
                        for (const checkbox of checkboxes) {
                            const chkId = checkbox.id;
                            const chkValue = checkbox.value || '';
                            
                            // 라벨 찾기
                            const label = document.querySelector(`label[for="${chkId}"]`);
                            if (label) {
                                const labelText = label.textContent.trim();
                                
                                if (labelText && !labelText.includes('전체')) {
                                    console.log(`        - ${labelText} (값: ${chkValue})`);
                                    
                                    result[btnId].children[secId].children.push({
                                        name: labelText,
                                        code: chkValue,
                                        id: chkId
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    console.log("\n" + "=" + "=".repeat(60));
    console.log("추출 완료!");
    console.log("=" + "=".repeat(60));
    
    // 통계
    const total1 = Object.keys(result).length;
    const total2 = Object.values(result).reduce((sum, v) => sum + Object.keys(v.children).length, 0);
    const total3 = Object.values(result).reduce((sum, v) => 
        sum + Object.values(v.children).reduce((s, c) => s + c.children.length, 0), 0
    );
    
    console.log(`\n통계:`);
    console.log(`  - 1차 분류: ${total1}개`);
    console.log(`  - 2차 분류: ${total2}개`);
    console.log(`  - 3차 분류: ${total3}개`);
    
    console.log("\n결과를 복사하려면:");
    console.log("copy(jobCategoriesResult)");
    
    return result;
}

// 실행
const jobCategoriesResult = await extractJobCategories();

// JSON 형식으로 출력
console.log("\n\nJSON 결과:");
console.log(JSON.stringify(jobCategoriesResult, null, 2));
