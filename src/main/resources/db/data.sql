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

MERGE INTO faq KEY (faq_no)
    VALUES
    (1, 'admin', '회원가입은 어떻게 하나요?', '회원가입은 메인화면에서 회원가입 버튼을 눌러주세요.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO qna KEY (qna_no)
    VALUES
    (1, 'silveruser', '급여는 언제 입금되나요?', '급여는 매월 말일에 지급됩니다.', FALSE, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
