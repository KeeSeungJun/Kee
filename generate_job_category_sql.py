import json

# JSON 파일 로드
with open('know_job_categories.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

sql_lines = []
sql_lines.append("-- KNOW 직업분류 데이터 (537개 직업)")
sql_lines.append("-- 대분류 10개, 중분류 34개")
sql_lines.append("")

# INSERT문 생성
insert_values = []

for major_code, major_data in sorted(data.items()):
    major_name = major_data['name']
    
    for middle_code, middle_data in sorted(major_data['children'].items()):
        middle_name = middle_data['name']
        
        for job in middle_data['jobs']:
            full_code = job['code']
            job_name = job['name']
            
            # SQL Injection 방지를 위한 이스케이프 처리
            job_name_escaped = job_name.replace("'", "''")
            major_name_escaped = major_name.replace("'", "''")
            middle_name_escaped = middle_name.replace("'", "''")
            
            insert_values.append(
                f"('{full_code}', '{job_name_escaped}', '{major_code}', '{major_name_escaped}', '{middle_code}', '{middle_name_escaped}')"
            )

# INSERT문 조합
sql_lines.append("INSERT INTO JOB_CATEGORY (")
sql_lines.append("    JOB_CODE, JOB_NAME, MAJOR_CODE, MAJOR_NAME, MIDDLE_CODE, MIDDLE_NAME")
sql_lines.append(") VALUES")

# 100개씩 끊어서 INSERT (MySQL의 max_allowed_packet 고려)
batch_size = 100
for i in range(0, len(insert_values), batch_size):
    batch = insert_values[i:i+batch_size]
    
    if i > 0:
        sql_lines.append(";")
        sql_lines.append("")
        sql_lines.append("INSERT INTO JOB_CATEGORY (")
        sql_lines.append("    JOB_CODE, JOB_NAME, MAJOR_CODE, MAJOR_NAME, MIDDLE_CODE, MIDDLE_NAME")
        sql_lines.append(") VALUES")
    
    sql_lines.append("    " + ",\n    ".join(batch))

sql_lines.append(";")
sql_lines.append("")

# 통계 출력
print(f"생성된 INSERT문 통계:")
print(f"  - 총 {len(insert_values)}개 직업")
print(f"  - {len(insert_values) // batch_size + 1}개 배치로 분할")

# 파일 저장
output_file = "job_category_insert.sql"
with open(output_file, 'w', encoding='utf-8') as f:
    f.write('\n'.join(sql_lines))

print(f"\n완료! 파일 저장: {output_file}")
