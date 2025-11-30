from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options
import time
import json

def setup_driver():
    """Chrome 드라이버 설정"""
    options = Options()
    options.add_argument('--headless')  # 백그라운드 실행
    options.add_argument('--no-sandbox')
    options.add_argument('--disable-dev-shm-usage')
    options.add_argument('user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36')
    
    driver = webdriver.Chrome(options=options)
    return driver

def scrape_job_categories():
    driver = setup_driver()
    
    try:
        url = "https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do"
        print(f"페이지 로딩 중: {url}")
        driver.get(url)
        
        # 페이지 로딩 대기
        time.sleep(3)
        
        job_data = {}
        
        # 1차 분류 버튼 찾기
        print("\n1차 분류 수집 중...")
        for i in range(1, 14):
            btn_id = f"btnjobName{i:02d}"
            
            try:
                btn = driver.find_element(By.ID, btn_id)
                btn_text = btn.text.strip()
                btn_value = btn.get_attribute('value') or ''
                
                print(f"  [{btn_id}] {btn_text} (값: {btn_value})")
                
                job_data[btn_id] = {
                    'name': btn_text,
                    'code': btn_value,
                    'children': {}
                }
                
                # 2차 분류 수집
                print(f"    2차 분류 수집 중...")
                btn.click()
                time.sleep(1)
                
                # 2차 분류 버튼 찾기 (동적으로 생성된 버튼들)
                # 패턴: btnjobName + 1차코드 + 추가문자
                second_buttons = driver.find_elements(By.CSS_SELECTOR, f"button[id^='btnjobName{i:02d}']")
                
                for sec_btn in second_buttons:
                    sec_id = sec_btn.get_attribute('id')
                    if sec_id != btn_id:  # 1차 버튼 제외
                        sec_text = sec_btn.text.strip()
                        sec_value = sec_btn.get_attribute('value') or ''
                        
                        print(f"      [{sec_id}] {sec_text} (값: {sec_value})")
                        
                        job_data[btn_id]['children'][sec_id] = {
                            'name': sec_text,
                            'code': sec_value,
                            'children': []
                        }
                        
                        # 3차 분류 수집
                        try:
                            sec_btn.click()
                            time.sleep(0.5)
                            
                            # 3차 분류 체크박스나 버튼 찾기
                            third_items = driver.find_elements(By.CSS_SELECTOR, "input[type='checkbox'][name*='job']")
                            
                            for item in third_items[:10]:  # 최대 10개만
                                item_id = item.get_attribute('id') or ''
                                item_value = item.get_attribute('value') or ''
                                
                                # 라벨 텍스트 찾기
                                label = driver.find_element(By.CSS_SELECTOR, f"label[for='{item_id}']")
                                label_text = label.text.strip() if label else ''
                                
                                if label_text:
                                    print(f"        - {label_text} (값: {item_value})")
                                    job_data[btn_id]['children'][sec_id]['children'].append({
                                        'name': label_text,
                                        'code': item_value
                                    })
                        except Exception as e:
                            print(f"        3차 분류 수집 오류: {e}")
                
            except Exception as e:
                print(f"  [{btn_id}] 찾기 실패: {e}")
                continue
        
        return job_data
        
    finally:
        driver.quit()

if __name__ == "__main__":
    print("=" * 60)
    print("고용24 직업분류 스크래핑 (Selenium)")
    print("=" * 60)
    
    result = scrape_job_categories()
    
    # JSON 저장
    output_file = "job_categories_work24.json"
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(result, f, ensure_ascii=False, indent=2)
    
    print("\n" + "=" * 60)
    print(f"완료! 결과 파일: {output_file}")
    print("=" * 60)
    
    # 통계
    total_first = len(result)
    total_second = sum(len(v['children']) for v in result.values())
    total_third = sum(
        len(child['children']) 
        for first in result.values() 
        for child in first['children'].values()
    )
    
    print(f"\n통계:")
    print(f"  - 1차 분류: {total_first}개")
    print(f"  - 2차 분류: {total_second}개")
    print(f"  - 3차 분류: {total_third}개")
