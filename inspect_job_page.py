import requests
from bs4 import BeautifulSoup

url = "https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do"

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
}

response = requests.get(url, headers=headers)
soup = BeautifulSoup(response.text, 'html.parser')

# HTML 구조 확인
print("=" * 60)
print("페이지 타이틀:", soup.title.string if soup.title else "없음")
print("=" * 60)

# 모든 button 태그 찾기
buttons = soup.find_all('button')
print(f"\n총 {len(buttons)}개의 버튼 발견\n")

# btnjobName으로 시작하는 버튼 찾기
job_buttons = [btn for btn in buttons if btn.get('id', '').startswith('btnjobName')]
print(f"btnjobName 버튼: {len(job_buttons)}개\n")

for btn in job_buttons[:20]:  # 처음 20개만 출력
    btn_id = btn.get('id', '')
    btn_text = btn.get_text(strip=True)
    btn_value = btn.get('value', '')
    btn_onclick = btn.get('onclick', '')
    
    print(f"ID: {btn_id}")
    print(f"  텍스트: {btn_text}")
    print(f"  값: {btn_value}")
    print(f"  onclick: {btn_onclick[:100] if btn_onclick else ''}")
    print()

# HTML 일부 저장
with open('job_page_sample.html', 'w', encoding='utf-8') as f:
    f.write(soup.prettify()[:50000])  # 처음 50000자만

print("\nHTML 샘플 저장됨: job_page_sample.html")
