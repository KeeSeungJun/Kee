-- 사용자 정보 테이블
CREATE TABLE IF NOT EXISTS USR_INFO (
      USR_NO                            INT AUTO_INCREMENT                      NOT NULL                                COMMENT '사용자 번호'
    , USR_ID                            VARCHAR(50)                             NOT NULL                                COMMENT '사용자 아이디(이메일)'
    , PASSWD                            VARCHAR(200)                            NOT NULL                                COMMENT '비밀번호'
    , USR_NM                            VARCHAR(50)                             NOT NULL                                COMMENT '사용자 이름'
    , USR_BIRTHDT                       DATE                                    NOT NULL                                COMMENT '생년월일'
    , USR_MBTLNUM                       VARCHAR(20)                                 NULL                                COMMENT '사용자 휴대폰번호'
    , USR_GRP_ID                        VARCHAR(50)                             NOT NULL                                COMMENT '사용자 그룹 아이디'
    , USE_AT                            VARCHAR(10)                             NOT NULL DEFAULT 'YES'                  COMMENT '사용 여부'
    , CREAT_ID                          VARCHAR(50)                             NOT NULL                                COMMENT '생성 ID'
    , CREAT_DT                          TIMESTAMP                               NOT NULL DEFAULT CURRENT_TIMESTAMP      COMMENT '생성 일시'
    , UPDT_ID                           VARCHAR(50)                             NOT NULL                                COMMENT '수정 ID'
    , UPDT_DT                           TIMESTAMP                               NOT NULL                                COMMENT '수정 일시'
    , USR_LAST_JOB                      VARCHAR(50)                             NULL                                    COMMENT '기존 직업'
    , USR_POST                          VARCHAR(50)                             NULL                                    COMMENT '우편번호'
    , USR_ADDR                          VARCHAR(255)                            NULL                                    COMMENT '주소'
    , USR_ADDR_LAT                      DECIMAL(10,7)                           NULL                                    COMMENT '주소 위도'
    , USR_ADDR_LON                      DECIMAL(10,7)                           NULL                                    COMMENT '주소 경도'
    , USR_JOB_SCR                       INT                                     NULL                                    COMMENT '직업 추천 점수'
    , USR_GENDER                        ENUM('M', 'F')                          NOT NULL DEFAULT 'M'                    COMMENT '성별, M : 남성, F : 여성'
    , USR_HEALTH                        VARCHAR(255)                            NOT NULL                                COMMENT '질병(8가지)'
--     , USR_HEALTH2                       ENUM('Y', 'N')                          NOT NULL DEFAULT 'N'                    COMMENT '당뇨병 Y : 있음, N : 정상'
--     , USR_HEALTH3                       ENUM('Y', 'N')                          NOT NULL DEFAULT 'N'                    COMMENT '목디스크 Y : 있음, N : 정상'
--     , USR_HEALTH4                       ENUM('Y', 'N')                          NOT NULL DEFAULT 'N'                    COMMENT '관절염 Y : 있음, N : 정상'
--     , USR_HEALTH5                       ENUM('Y', 'N')                          NOT NULL DEFAULT 'N'                    COMMENT '만성요통 Y : 있음, N : 정상'
--     , USR_HEALTH6                       ENUM('Y', 'N')                          NOT NULL DEFAULT 'N'                    COMMENT '심장질환 Y : 있음, N : 정상'
--     , USR_HEALTH7                       ENUM('Y', 'N')                          NOT NULL DEFAULT 'N'                    COMMENT '시력저하 Y : 있음, N : 정상'
--     , USR_HEALTH8                       ENUM('Y', 'N')                          NOT NULL DEFAULT 'N'                    COMMENT '청력저하 Y : 있음, N : 정상'
    , USR_HEALTH_ETC                    VARCHAR(250)                            NULL                                    COMMENT '기타 질병사항들'


    , CONSTRAINT PK_USR_INFO PRIMARY KEY (USR_NO)
    , UNIQUE INDEX UK_USR_INFO01 (USR_ID, USE_AT)
    , INDEX IDX_USR_INFO01 (USR_ID)
    , INDEX IDX_USR_INFO02 (USR_GRP_ID)
    , INDEX IDX_USR_INFO97 (USE_AT)
    , INDEX IDX_USR_INFO98 (CREAT_DT)
    , INDEX IDX_USR_INFO99 (UPDT_DT)
    ) AUTO_INCREMENT=1000000001 COMMENT = '사용자 정보';

-- 사용자 정보 데이터, 비밀번호 : qwer1234
INSERT INTO USR_INFO (USR_NO, USR_ID, PASSWD, USR_NM, USR_BIRTHDT, USR_MBTLNUM, USR_GRP_ID,
                      USE_AT, CREAT_ID, CREAT_DT, UPDT_ID, UPDT_DT,
                      USR_JOB_SCR, USR_ADDR, USR_ADDR_LAT, USR_ADDR_LON,
                      USR_GENDER,
                      USR_HEALTH, USR_HEALTH_ETC) VALUES
  (1000000001, 'admin', '{bcrypt}$2a$10$2IeGPuGnzDVrv5nPTSIrHeHnv7ua9csBUq7B1b3gK6uuhY.K3bxW2', '어드민', '1970-01-01',  '01012345678', 'ADMIN',    'admin', 'admin', NOW(), '학생', NOW(), '70', '서울시 강남구 영동대로22', '37.4907910', '127.0750830', 'M', '고혈압', '천식')
, (1000000002, 'test',  '{bcrypt}$2a$10$2IeGPuGnzDVrv5nPTSIrHeHnv7ua9csBUq7B1b3gK6uuhY.K3bxW2', '테스트', '1970-01-01',  '01012345678', 'CUSTOMER', 'admin', 'admin', NOW(), '학생', NOW(), '70', '서울시 강남구 영동대로22', '37.4907910', '127.0750830', 'M', '고혈압', '천식')
, (1000000003, 'guest', '{bcrypt}$2a$10$2IeGPuGnzDVrv5nPTSIrHeHnv7ua9csBUq7B1b3gK6uuhY.K3bxW2', '게스트', '1970-01-01',  '01012345678', 'GUEST',    'admin', 'admin', NOW(), '학생', NOW(), '70', '서울시 강남구 영동대로22', '37.4907910', '127.0750830', 'M', '고혈압', '천식')
, (1000000004, 'test',  '{bcrypt}$2a$10$2IeGPuGnzDVrv5nPTSIrHeHnv7ua9csBUq7B1b3gK6uuhY.K3bxW2', 'test1', '1970-01-01',  '01012345678', 'GUEST',    'admin', 'admin', NOW(), '학생', NOW(), '70', '서울시 강남구 영동대로22', '37.4907910', '127.0750830', 'M', '고혈압', '천식')

    ON DUPLICATE KEY UPDATE
      PASSWD                        = VALUES(PASSWD)
    , USR_NM                        = VALUES(USR_NM)
    , USR_GRP_ID                    = VALUES(USR_GRP_ID)
    , UPDT_ID                       = VALUES(UPDT_ID)
    , UPDT_DT                       = NOW()
;

CREATE TABLE IF NOT EXISTS JOB_INFO (
      JOB_NO                              INT AUTO_INCREMENT                      PRIMARY KEY                           COMMENT '직업 번호'
    , USR_ID                              VARCHAR(50)                             NOT NULL                              COMMENT '유저 아이디(이메일)'
    , JOB_TITLE                           VARCHAR(50)                             NOT NULL                              COMMENT '직업 이름'
    , JOB_TASK                            VARCHAR(50)                             NOT NULL                              COMMENT '업무 명'
    , JOB_DESC                            VARCHAR(255)                            NOT NULL                              COMMENT '직무 상세 내용'
    , JOB_LICENSE_REQUIRED                VARCHAR(255)                            NOT NULL                              COMMENT '필수 자격·면허 (콤마로 구분)'
    , JOB_CONTRACT_PERIOD                 VARCHAR(50)                             NOT NULL                              COMMENT '계약 기간 (예: 6개월, 재계약 가능)'
    , JOB_SALARY                          VARCHAR(50)                             NOT NULL                              COMMENT '급여 정보 (예: 209만원 이상, 협의 가능)'
    , JOB_WORK_HOURS                      VARCHAR(100)                            NOT NULL                              COMMENT '근무 시간 (예: 08:00~18:00, 주 5일)'
    , JOB_BREAK_TIME                      VARCHAR(50)                             NOT NULL                              COMMENT '휴게 시간 (예: 2시간)'
    , JOB_BENEFITS                        VARCHAR(255)                            NOT NULL                              COMMENT '복지 조건 (보험, 퇴직금 등)'
    , JOB_ADDRESS                         VARCHAR(255)                            NOT NULL                              COMMENT '근무지 주소'
    , JOB_LATITUDE                        DOUBLE                                  NOT NULL                              COMMENT '위도 (KakaoMap API로 얻음)'
    , JOB_LONGITUDE                       DOUBLE                                  NOT NULL                              COMMENT '경도 (KakaoMap API로 얻음)'
    , JOB_NEARBY_SUBWAY                   VARCHAR(50)                             NOT NULL                              COMMENT '인근 전철·역 (예: 용문역 1km)'
    , JOB_BUS_ROUTES                      VARCHAR(50)                             NOT NULL                              COMMENT '버스 노선 번호 (예: 315,318,602)'
    , CREATED_AT                          TIMESTAMP                               DEFAULT CURRENT_TIMESTAMP             COMMENT '생성 일시'
    , UPDATED_AT                          TIMESTAMP                               DEFAULT CURRENT_TIMESTAMP             COMMENT '수정 일시'
--     CONSTRAINT FK_JOB_USER FOREIGN KEY (USR_ID) REFERENCES USR_INFO(USR_ID)
    );
-- -- group_info 테이블 생성
-- CREATE TABLE IF NOT EXISTS group_info (
--     group_no                          INT UNSIGNED AUTO_INCREMENT             NOT NULL                                COMMENT '그룹 번호',
--     group_id INT NOT NULL,
--     group_nm VARCHAR(255) NOT NULL,
--     CREAT_DT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     UPDT_DT TIMESTAMP NOT NULL ,
--     CONSTRAINT PK_GROUP_INFO PRIMARY KEY (group_no),
--     UNIQUE INDEX UK_GROUP_INFO01 (group_id),
--     UNIQUE INDEX UK_GROUP_INFO02 (group_nm)
--     ) AUTO_INCREMENT=1000000001 COMMENT = '그룹 정보';
--
-- INSERT INTO group_info (group_no, group_id, group_nm, UPDT_DT) VALUES
--   (1000000001, 0, '어드민', NOW())
-- , (1000000002, 1, '시니어', NOW())
--     ON DUPLICATE KEY UPDATE
--   group_id                        = VALUES(group_id)
-- , group_nm                        = VALUES(group_nm)
-- , UPDT_DT                         = NOW()
-- ;
--
--
--
--
-- -- user_info 테이블 생성
-- CREATE TABLE IF NOT EXISTS user_info (
--     user_no BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_id VARCHAR(255) NOT NULL,
--     user_nm VARCHAR(255),
--     group_id INT,
--     user_addr VARCHAR(255),
--     user_addr_lat DECIMAL(10,6),
--     user_addr_lon DECIMAL(10,6),
--     user_gender CHAR(1),
--     user_phone VARCHAR(20),
--     user_job_score INT,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_user_group FOREIGN KEY (group_id) REFERENCES group_info(group_id)
--     );
--
-- -- user_health 테이블 생성
-- CREATE TABLE IF NOT EXISTS user_health (
--     user_health_no BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_id VARCHAR(255) NOT NULL,
--     user_health1_YN BOOLEAN,
--     user_health2_YN BOOLEAN,
--     user_health3_YN BOOLEAN,
--     user_health4_YN BOOLEAN,
--     user_health5_YN BOOLEAN,
--     user_health6_YN BOOLEAN,
--     user_health7_YN BOOLEAN,
--     user_health8_YN BOOLEAN,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_user_health FOREIGN KEY (user_id) REFERENCES user_info(user_id)
--     );





-- -- user_job 테이블 생성
-- CREATE TABLE IF NOT EXISTS user_job (
--                                         user_job_no BIGINT AUTO_INCREMENT PRIMARY KEY,
--                                         user_id VARCHAR(255) NOT NULL,
--     job_no BIGINT NOT NULL,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_user_job_user FOREIGN KEY (user_id) REFERENCES user_info(user_id),
--     CONSTRAINT fk_user_job_job FOREIGN KEY (job_no) REFERENCES job_info(job_no)
--     );
--
-- -- faq 테이블 생성
-- CREATE TABLE IF NOT EXISTS FAQ (
--                                    faq_no BIGINT AUTO_INCREMENT PRIMARY KEY,
--                                    user_id VARCHAR(255) NOT NULL,
--     faq_title VARCHAR(255),
--     faq_body TEXT,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_faq_user FOREIGN KEY (user_id) REFERENCES user_info(user_id)
--     );
--
-- -- qna 테이블 생성
-- CREATE TABLE IF NOT EXISTS QNA (
--     qna_no BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_id VARCHAR(255) NOT NULL,
--     qna_title VARCHAR(255),
--     qna_body TEXT,
--     qna_comment_yn BOOLEAN DEFAULT FALSE,
--     qna_comment_body TEXT,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_qna_user FOREIGN KEY (user_id) REFERENCES user_info(user_id)
--     );
