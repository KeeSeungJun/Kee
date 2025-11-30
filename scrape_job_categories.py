import requests
from bs4 import BeautifulSoup
import json
import time

# 고용24 직업분류 페이지
base_url = "https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do"

session = requests.Session()
session.headers.update({
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
})

def get_job_categories():
    """1차 분류 가져오기"""
    print("1차 분류 수집 중...")
    
    response = session.get(base_url)
    soup = BeautifulSoup(response.text, 'html.parser')
    
    job_data = {}
    
    # 1차 분류 버튼 찾기 (btnjobName01 ~ btnjobName13)
    for i in range(1, 14):
        btn_id = f"btnjobName{i:02d}"
        btn = soup.find('button', {'id': btn_id})
        
        if btn:
            job_name = btn.get_text(strip=True)
            job_code = btn.get('value', '')
            
            print(f"  [{btn_id}] {job_name} (코드: {job_code})")
            
            job_data[btn_id] = {
                'name': job_name,
                'code': job_code,
                'children': {}
            }
    
    return job_data

def get_second_level(first_code, first_btn_id):
    """2차 분류 가져오기"""
    print(f"\n2차 분류 수집 중 (1차: {first_btn_id})...")
    
    # AJAX 요청으로 2차 분류 데이터 가져오기
    # 실제 고용24의 AJAX 엔드포인트를 찾아야 함
    # 임시로 페이지 파싱 시도
    
    params = {
        'occupation': first_code
    }
    
    try:
        response = session.get(base_url, params=params)
        soup = BeautifulSoup(response.text, 'html.parser')
        
        second_level = {}
        
        # 2차 분류 버튼 패턴 찾기
        # btnjobName084, btnjobName01D 등의 패턴
        buttons = soup.find_all('button', id=lambda x: x and x.startswith('btnjobName'))
        
        for btn in buttons:
            btn_id = btn.get('id')
            # 1차 코드로 시작하는 2차 분류만 필터링
            if btn_id and btn_id.startswith(f'btnjobName{first_btn_id[10:]}'):
                if btn_id != first_btn_id:  # 1차 분류 자신은 제외
                    job_name = btn.get_text(strip=True)
                    job_code = btn.get('value', '')
                    
                    print(f"    [{btn_id}] {job_name} (코드: {job_code})")
                    
                    second_level[btn_id] = {
                        'name': job_name,
                        'code': job_code,
                        'children': []
                    }
        
        time.sleep(0.5)  # 서버 부하 방지
        return second_level
        
    except Exception as e:
        print(f"    오류 발생: {e}")
        return {}

def scrape_all_categories():
    """전체 직업분류 스크래핑"""
    print("=" * 60)
    print("고용24 직업분류 스크래핑 시작")
    print("=" * 60)
    
    # 1차 분류
    job_data = get_job_categories()
    
    # 2차 분류
    for first_id, first_data in job_data.items():
        first_code = first_data['code']
        if first_code:
            second_level = get_second_level(first_code, first_id)
            first_data['children'] = second_level
    
    return job_data

if __name__ == "__main__":
    result = scrape_all_categories()
    
    # JSON 파일로 저장
    output_file = "job_categories_work24.json"
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(result, f, ensure_ascii=False, indent=2)
    
    print("\n" + "=" * 60)
    print(f"스크래핑 완료! 결과 파일: {output_file}")
    print("=" * 60)
    
    # 통계 출력
    total_first = len(result)
    total_second = sum(len(v['children']) for v in result.values())
    
    print(f"\n통계:")
    print(f"  - 1차 분류: {total_first}개")
    print(f"  - 2차 분류: {total_second}개")
