import csv
import json
from collections import defaultdict

def parse_csv_files():
    """CSV 파일 파싱하여 계층 구조 생성"""
    
    # 중분류 데이터 로드
    middle_categories = {}
    with open('c:/Users/EZ/Desktop/졸업작품관련/한국고용정보원_워크넷_직업분류_20230818/직업중분류.CSV', 
              'r', encoding='cp949') as f:
        reader = csv.DictReader(f)
        for row in reader:
            code = f"{row['KNOW직업대분류']}{row['KNOW직업중분류']}"
            middle_categories[code] = row['KNOW직업중분류명']
    
    # 세세분류 데이터 로드 및 계층 구조 생성
    job_hierarchy = defaultdict(lambda: defaultdict(list))
    
    with open('c:/Users/EZ/Desktop/졸업작품관련/한국고용정보원_워크넷_직업분류_20230818/직업세세분류.CSV', 
              'r', encoding='cp949') as f:
        reader = csv.DictReader(f)
        
        for row in reader:
            # 코드 조합
            대분류 = row['KNOW직업대분류']
            중분류 = row['KNOW직업중분류']
            소분류 = row['KNOW직업소분류']
            세분류 = row['KNOW직업세분류']
            세세분류 = row['KNOW직업세세분류']
            
            중분류코드 = f"{대분류}{중분류}"
            전체코드 = f"{대분류}{중분류}{소분류}{세분류}{세세분류}"
            
            직업명 = row['KNOW직업명']
            
            # 계층 구조에 추가
            job_hierarchy[대분류][중분류코드].append({
                'code': 전체코드,
                'name': 직업명,
                '소분류': 소분류,
                '세분류': 세분류,
                '세세분류': 세세분류
            })
    
    # 결과 정리
    result = {}
    
    for 대분류, 중분류_dict in sorted(job_hierarchy.items()):
        대분류명 = get_대분류명(대분류)
        
        result[대분류] = {
            'name': 대분류명,
            'code': 대분류,
            'children': {}
        }
        
        for 중분류코드, 직업목록 in sorted(중분류_dict.items()):
            중분류명 = middle_categories.get(중분류코드, '미분류')
            
            result[대분류]['children'][중분류코드] = {
                'name': 중분류명,
                'code': 중분류코드,
                'jobs': sorted(직업목록, key=lambda x: x['code'])
            }
    
    return result

def get_대분류명(code):
    """대분류 코드를 이름으로 변환"""
    mapping = {
        '0': '관리자',
        '1': '전문가 및 관련 종사자',
        '2': '사무 종사자',
        '3': '서비스 종사자',
        '4': '판매 종사자',
        '5': '농림어업 숙련 종사자',
        '6': '기능원 및 관련 기능 종사자',
        '7': '장치·기계 조작 및 조립 종사자',
        '8': '단순노무 종사자',
        '9': '군인'
    }
    return mapping.get(code, f'분류{code}')

if __name__ == "__main__":
    print("=" * 60)
    print("CSV 파일 분석 중...")
    print("=" * 60)
    
    result = parse_csv_files()
    
    # JSON 저장
    output_file = "know_job_categories.json"
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(result, f, ensure_ascii=False, indent=2)
    
    print(f"\n완료! 결과 파일: {output_file}")
    
    # 통계
    total_대분류 = len(result)
    total_중분류 = sum(len(v['children']) for v in result.values())
    total_직업 = sum(
        len(child['jobs'])
        for category in result.values()
        for child in category['children'].values()
    )
    
    print(f"\n통계:")
    print(f"  - 대분류: {total_대분류}개")
    print(f"  - 중분류: {total_중분류}개")
    print(f"  - 직업: {total_직업}개")
    
    print(f"\n대분류 목록:")
    for code, data in sorted(result.items()):
        중분류수 = len(data['children'])
        직업수 = sum(len(c['jobs']) for c in data['children'].values())
        print(f"  [{code}] {data['name']} - 중분류 {중분류수}개, 직업 {직업수}개")
