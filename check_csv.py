import csv

# 중분류 파일 확인
print("=" * 60)
print("직업중분류.CSV 파일 확인")
print("=" * 60)

try:
    with open('c:/Users/EZ/Desktop/졸업작품관련/한국고용정보원_워크넷_직업분류_20230818/직업중분류.CSV', 
              'r', encoding='utf-8-sig') as f:
        reader = csv.DictReader(f)
        print(f"컬럼명: {reader.fieldnames}")
        
        for i, row in enumerate(reader):
            if i < 5:
                print(f"\n행 {i+1}:")
                for key, value in row.items():
                    print(f"  {key}: {value}")
            else:
                break
except Exception as e:
    print(f"UTF-8 오류: {e}")
    
    # cp949 시도
    try:
        with open('c:/Users/EZ/Desktop/졸업작품관련/한국고용정보원_워크넷_직업분류_20230818/직업중분류.CSV', 
                  'r', encoding='cp949') as f:
            reader = csv.DictReader(f)
            print(f"컬럼명 (cp949): {reader.fieldnames}")
            
            for i, row in enumerate(reader):
                if i < 5:
                    print(f"\n행 {i+1}:")
                    for key, value in row.items():
                        print(f"  {key}: {value}")
                else:
                    break
    except Exception as e2:
        print(f"CP949 오류: {e2}")

print("\n" + "=" * 60)
print("직업세세분류.CSV 파일 확인")
print("=" * 60)

try:
    with open('c:/Users/EZ/Desktop/졸업작품관련/한국고용정보원_워크넷_직업분류_20230818/직업세세분류.CSV', 
              'r', encoding='utf-8-sig') as f:
        reader = csv.DictReader(f)
        print(f"컬럼명: {reader.fieldnames}")
        
        for i, row in enumerate(reader):
            if i < 3:
                print(f"\n행 {i+1}:")
                for key, value in row.items():
                    print(f"  {key}: {value}")
            else:
                break
except Exception as e:
    print(f"UTF-8 오류: {e}")
