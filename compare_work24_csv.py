import requests
from bs4 import BeautifulSoup
import csv
from collections import defaultdict

def get_work24_job_text():
    """고용24 페이지에서 모든 텍스트 추출"""
    url = "https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do"
    
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
    }
    
    response = requests.get(url, headers=headers)
    soup = BeautifulSoup(response.text, 'html.parser')
    
    # 모든 텍스트 추출
    all_text = soup.get_text()
    
    # 줄바꿈 기준으로 분리하고 공백 제거
    lines = [line.strip() for line in all_text.split('\n') if line.strip()]
    
    return lines, soup

def load_know_categories():
    """KNOW CSV 파일에서 직업명 로드"""
    중분류_dict = {}
    
    # 중분류 로드
    with open('c:/Users/EZ/Desktop/졸업작품관련/한국고용정보원_워크넷_직업분류_20230818/직업중분류.CSV', 
              'r', encoding='cp949') as f:
        reader = csv.DictReader(f)
        for row in reader:
            중분류_dict[row['KNOW직업중분류명']] = row
    
    # 세세분류 로드
    직업_list = []
    with open('c:/Users/EZ/Desktop/졸업작품관련/한국고용정보원_워크넷_직업분류_20230818/직업세세분류.CSV', 
              'r', encoding='cp949') as f:
        reader = csv.DictReader(f)
        for row in reader:
            직업_list.append(row['KNOW직업명'])
    
    return 중분류_dict, 직업_list

def compare_with_csv():
    """고용24 페이지와 CSV 데이터 비교"""
    print("=" * 70)
    print("고용24 페이지 텍스트 추출 중...")
    print("=" * 70)
    
    # 고용24 페이지 텍스트
    work24_lines, soup = get_work24_job_text()
    work24_text = '\n'.join(work24_lines)
    
    print(f"추출된 텍스트 라인 수: {len(work24_lines)}")
    
    # CSV 데이터 로드
    print("\nKNOW CSV 데이터 로드 중...")
    중분류_dict, 직업_list = load_know_categories()
    
    print(f"KNOW 중분류: {len(중분류_dict)}개")
    print(f"KNOW 직업: {len(직업_list)}개")
    
    # 비교 분석
    print("\n" + "=" * 70)
    print("비교 분석 결과")
    print("=" * 70)
    
    # 중분류 매칭
    print("\n[중분류 매칭 결과]")
    matched_중분류 = []
    for 중분류명 in 중분류_dict.keys():
        if 중분류명 in work24_text:
            matched_중분류.append(중분류명)
            print(f"  ✓ {중분류명}")
    
    print(f"\n매칭된 중분류: {len(matched_중분류)}/{len(중분류_dict)} ({len(matched_중분류)/len(중분류_dict)*100:.1f}%)")
    
    # 직업명 샘플 매칭 (전체는 너무 많으므로)
    print("\n[직업명 샘플 매칭 결과 (처음 50개)]")
    matched_직업 = []
    for i, 직업명 in enumerate(직업_list[:50]):
        if 직업명 in work24_text:
            matched_직업.append(직업명)
            print(f"  ✓ {직업명}")
        else:
            print(f"  ✗ {직업명}")
    
    print(f"\n샘플 매칭 비율: {len(matched_직업)}/50 ({len(matched_직업)/50*100:.1f}%)")
    
    # 페이지에서 직업 관련 섹션 찾기
    print("\n" + "=" * 70)
    print("페이지 내 직업 관련 요소 분석")
    print("=" * 70)
    
    # 직업 관련 버튼/링크 찾기
    job_buttons = soup.find_all(['button', 'a', 'label'], text=lambda t: t and any(
        keyword in str(t) for keyword in ['관리자', '전문가', '사무', '서비스', '판매']
    ))
    
    print(f"\n직업 관련 요소 {len(job_buttons)}개 발견:")
    for elem in job_buttons[:20]:
        print(f"  <{elem.name}> {elem.get_text(strip=True)[:50]}")
    
    # HTML 샘플 저장
    with open('work24_page_sample.html', 'w', encoding='utf-8') as f:
        f.write(soup.prettify())
    
    print(f"\n전체 HTML 저장됨: work24_page_sample.html")
    
    # 결론
    print("\n" + "=" * 70)
    print("결론")
    print("=" * 70)
    
    if len(matched_중분류) / len(중분류_dict) > 0.7:
        print("✅ 고용24 페이지가 KNOW 직업분류 체계를 사용하는 것으로 보입니다.")
    else:
        print("❌ 고용24 페이지가 다른 분류 체계를 사용하거나,")
        print("   직업 분류가 동적으로 로드되는 것 같습니다.")
    
    return matched_중분류, matched_직업

if __name__ == "__main__":
    matched_중분류, matched_직업 = compare_with_csv()
