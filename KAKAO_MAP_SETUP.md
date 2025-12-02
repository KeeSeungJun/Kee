# 📍 KakaoMap API 연동 가이드

## 🎯 목적
일자리 근무지 주소를 위도/경도로 변환하여 사용자와의 거리 계산 및 추천 정확도 향상

---

## 📝 설정 방법

### 1. Kakao Developers 계정 생성 및 API 키 발급

1. https://developers.kakao.com/ 접속
2. 로그인 후 "내 애플리케이션" 클릭
3. "애플리케이션 추가하기" 클릭
4. 앱 정보 입력 후 생성
5. "앱 설정" > "앱 키" 에서 **REST API 키** 복사

### 2. application.yaml 설정

```yaml
kakao:
  api:
    key: YOUR_KAKAO_REST_API_KEY  # 발급받은 REST API 키로 교체
```

---

## 🚀 사용 방법

### 1. 전체 일자리 위치 정보 일괄 업데이트

```http
GET /api/admin/job-location/update-all
```

**설명:**
- 2,173개 일자리의 주소를 위도/경도로 변환하여 DB 저장
- 백그라운드에서 실행 (약 3-5분 소요)
- API 호출 제한: 초당 10건 (sleep 100ms)

**응답:**
```json
{
  "code": 200,
  "message": "일자리 위치 정보 업데이트가 시작되었습니다. 백그라운드에서 처리됩니다."
}
```

### 2. 특정 일자리 위치 정보 업데이트

```http
GET /api/admin/job-location/update/{jobNo}
```

**예시:**
```http
GET /api/admin/job-location/update/1
```

**응답:**
```json
{
  "code": 200,
  "message": "일자리 위치 정보가 업데이트되었습니다."
}
```

---

## 📊 데이터 구조

### JOB_INFO 테이블

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| JOB_WORK_LOCATION | VARCHAR(255) | 근무지 주소 (입력) |
| JOB_LOCATION_LAT | DECIMAL(10,7) | 근무지 위도 (자동 계산) |
| JOB_LOCATION_LON | DECIMAL(10,7) | 근무지 경도 (자동 계산) |

**예시 데이터:**
```
근무지: 대전광역시 서구 유등로 507(용문동)
위도: 36.337764
경도: 127.383136
```

---

## 🎨 OpenAI 프롬프트 개선

### 변경 전 (하드코딩)
```
'승합차 운전원', '차량 운행', ..., '36.346846', '127.375863', ...
```

### 변경 후 (DB 동적 생성)
```
{{JOB_LIST}} 플레이스홀더 사용
→ 실제 DB 데이터로 자동 치환
→ 거리 계산 가능 (Haversine 공식)
```

### 프롬프트 필드 (11개)
1. 모집 직종
2. 채용공고 제목
3. 기업명
4. 고용형태
5. 급여 정보
6. 근무 시간
7. 우대 조건
8. 근무지 주소
9. **위도** ✨ (NEW)
10. **경도** ✨ (NEW)
11. 인근 전철역

---

## ⚡ 성능 최적화

### DB 저장 시점 선택 (현재 구현: ✅ 방법 1)

#### 방법 1: DB 저장 시 미리 계산 (권장 ✅)
- **장점:** 추천 시 빠른 응답 속도, API 호출 0번
- **단점:** 초기 저장 시간 증가 (약 3-5분)
- **결정:** 채택

#### 방법 2: 추천 시 실시간 계산
- **장점:** 초기 저장 빠름
- **단점:** 추천 시마다 2,173번 API 호출 → 매우 느림
- **결정:** 미채택

---

## 🔧 주요 클래스

### 1. KakaoMapService
```java
// 주소 → 좌표 변환
Map<String, Double> coordinates = kakaoMapService.getCoordinates("대전광역시 서구 유등로 507");
// { latitude: 36.337764, longitude: 127.383136 }

// 거리 계산 (Haversine 공식)
double distance = kakaoMapService.calculateDistance(lat1, lon1, lat2, lon2);
// 단위: km
```

### 2. JobLocationUpdateService
```java
// 전체 일자리 좌표 업데이트
jobLocationUpdateService.updateAllJobLocations();

// 특정 일자리 좌표 업데이트
jobLocationUpdateService.updateJobLocation(jobNo);
```

### 3. OpenAiService
```java
// buildJobListPrompt() - DB 데이터를 프롬프트 형식으로 변환
String jobListPrompt = buildJobListPrompt(jobs);
// 위도/경도 포함하여 CSV 형식 생성
```

---

## 📈 예상 효과

### 추천 정확도 향상
- **거리 기반 필터링**: 사용자 거주지에서 20km 이내 일자리 우선 추천
- **대중교통 고려**: 인근 전철역 정보 활용
- **실시간 계산**: AI가 위도/경도로 정확한 거리 계산

### 사용자 경험 개선
- **빠른 응답 속도**: DB에 미리 저장되어 즉시 응답
- **맞춤형 추천**: 출퇴근 거리 고려한 현실적 추천
- **신뢰도 증가**: 구체적인 거리 정보 제공

---

## ⚠️ 주의사항

1. **API 할당량**: Kakao REST API 무료 플랜 일일 한도 확인
2. **초기 실행**: 서버 시작 후 `/api/admin/job-location/update-all` 1회 실행 필수
3. **새 일자리 등록 시**: 자동으로 좌표 업데이트 또는 수동 API 호출
4. **좌표 정확도**: 일부 주소는 변환 실패 가능 (`-` 값 유지)

---

## 🎯 다음 단계

1. [ ] Kakao API 키 발급
2. [ ] application.yaml에 API 키 설정
3. [ ] 서버 재시작
4. [ ] `/api/admin/job-location/update-all` API 호출
5. [ ] 좌표 업데이트 완료 확인 (로그)
6. [ ] 일자리 추천 테스트

