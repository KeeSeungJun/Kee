-- =============================================
-- ⚠️ 중요: 이 파일은 애플리케이션 실행 시 자동으로 실행되지 않습니다.
--
-- 현재 프로젝트는 AWS RDS MySQL을 사용하며,
-- 이 파일은 H2 DB 전용 문법(MERGE INTO)으로 작성되어 있습니다.
--
-- MySQL에서 사용하려면:
-- - MERGE INTO → INSERT ... ON DUPLICATE KEY UPDATE 또는 REPLACE INTO로 변경 필요
--
-- 이 파일의 용도:
-- 1. 샘플 데이터 참고용
-- 2. 로컬 테스트 환경 구축 시 참고
-- =============================================

MERGE INTO group_info KEY (group_id)
    VALUES
    (1, 0, 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, 'SILVER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO user_info KEY (user_id)
    VALUES
    (1000000001, 'admin', '관리자', 0, '서울시 강남구', 37.4979, 127.0276, 'M', '01012345678', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1000000002, 'silveruser', '실버회원', 1, '서울시 서초구', 37.4838, 127.0324, 'F', '01087654321', 85, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO user_health KEY (user_id)
    VALUES
    (1, 'admin', FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'silveruser', TRUE, TRUE, FALSE, FALSE, TRUE, FALSE, TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO job_info KEY (job_no)
    VALUES
    (1, 'admin', '도서 정리 보조', '도서관 정리', '도서관 책 정리 보조 업무입니다.', '문화', '서울시 종로구', 37.5720, 126.9794, 'http://job.example.com/1', '시급 12000원', '027778888', 'A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO user_job KEY (user_job_no)
    VALUES
    (1, 'silveruser', 1, CURRENT_TIMESTAMP);

MERGE INTO FAQ_INFO KEY (FAQ_NO)
    VALUES
    (1, 'account', '회원가입은 어떻게 하나요?', '회원가입은 메인화면에서 회원가입 버튼을 눌러주세요.', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin'),
    (2, 'apply', '일자리 지원은 어떻게 하나요?', '일자리 상세 페이지에서 지원하기 버튼을 클릭하세요.', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin'),
    (3, 'salary', '급여 지급일은 언제인가요?', '각 업체별 근로 계약서에 명시된 날짜에 지급됩니다.', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin'),
    (4, 'etc', '앱 사용 중 오류가 발생하면 어떻게 하나요?', '문의하기 게시판을 통해 신고해주시면 빠르게 처리하겠습니다.', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin');

MERGE INTO QNA_INFO KEY (QNA_NO)
    VALUES
    (1, 'test', '회원가입', '비밀번호 변경이 안됩니다.', '비밀번호는 프로필 설정에서 변경 가능합니다.', 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin'),
    (2, 'guest', '일자리 지원', '일자리 지원 후 연락은 언제 오나요?', '보통 1주일 이내에 연락 드립니다.', 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin'),
    (3, 'test', '프로필 문의', '프로필 변경 방법 알려주세요.', NULL, 'WAITING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL),
    (4, 'guest', '앱 오류', '앱 설치가 안돼요.', NULL, 'WAITING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL);

