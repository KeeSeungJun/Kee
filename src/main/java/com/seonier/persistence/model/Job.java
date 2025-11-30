package com.seonier.persistence.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JOB_INFO 테이블 매핑 엔티티 (고용24 채용공고)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("JOB_INFO")
public class Job {

    @Id
    private Integer jobNo;                      // JOB_NO

    // 기본 정보
    private String usrId;                       // USR_ID (EMPLOYMENT24 고정)
    private String jobTitle;                    // JOB_TITLE (채용공고 제목)
    private String jobCompanyName;              // JOB_COMPANY_NAME (기업명)
    private String jobDetailUrl;                // JOB_DETAIL_URL (상세페이지 URL)
    private String jobDday;                     // JOB_DDAY (D-Day)
    private LocalDate jobDeadline;              // JOB_DEADLINE (마감일)
    private String jobSalaryPreview;            // JOB_SALARY_PREVIEW (급여 미리보기)
    
    // 상세 정보
    private String jobPosition;                 // JOB_POSITION (모집 직종)
    private String jobRecruitCount;             // JOB_RECRUIT_COUNT (모집 인원)
    private String jobExperience;               // JOB_EXPERIENCE (경력)
    private String jobEducation;                // JOB_EDUCATION (학력)
    private String jobEmploymentType;           // JOB_EMPLOYMENT_TYPE (고용형태)
    private String jobWorkLocation;             // JOB_WORK_LOCATION (근무 예정지)
    private String jobSalary;                   // JOB_SALARY (임금 조건)
    private String jobRank;                     // JOB_RANK (채용 직급)
    private String jobPreference;               // JOB_PREFERENCE (우대 조건)
    private String jobWorkType;                 // JOB_WORK_TYPE (근무 형태)
    private String jobWorkHours;                // JOB_WORK_HOURS (근무 시간)
    private String jobNearbyStation;            // JOB_NEARBY_STATION (인근 전철역)
    
    // 시스템 정보
    private LocalDateTime createdAt;            // CREATED_AT
    private LocalDateTime updatedAt;            // UPDATED_AT

}
