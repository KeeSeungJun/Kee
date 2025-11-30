package com.seonier.persistence.mapper;

import com.seonier.persistence.model.Job;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface JobMapper {
    
    /**
     * 전체 채용공고 조회
     */
    @Select("""
        SELECT
          JOB_NO              AS jobNo,
          USR_ID              AS usrId,
          JOB_TITLE           AS jobTitle,
          JOB_COMPANY_NAME    AS jobCompanyName,
          JOB_DETAIL_URL      AS jobDetailUrl,
          JOB_DDAY            AS jobDday,
          JOB_DEADLINE        AS jobDeadline,
          JOB_SALARY_PREVIEW  AS jobSalaryPreview,
          JOB_POSITION        AS jobPosition,
          JOB_RECRUIT_COUNT   AS jobRecruitCount,
          JOB_EXPERIENCE      AS jobExperience,
          JOB_EDUCATION       AS jobEducation,
          JOB_EMPLOYMENT_TYPE AS jobEmploymentType,
          JOB_WORK_LOCATION   AS jobWorkLocation,
          JOB_SALARY          AS jobSalary,
          JOB_RANK            AS jobRank,
          JOB_PREFERENCE      AS jobPreference,
          JOB_WORK_TYPE       AS jobWorkType,
          JOB_WORK_HOURS      AS jobWorkHours,
          JOB_NEARBY_STATION  AS jobNearbyStation,
          CREATED_AT          AS createdAt,
          UPDATED_AT          AS updatedAt
        FROM JOB_INFO
        ORDER BY CREATED_AT DESC
        """)
    List<Job> findAll();

    /**
     * 채용공고 번호로 조회
     */
    @Select("""
        SELECT
          JOB_NO              AS jobNo,
          USR_ID              AS usrId,
          JOB_TITLE           AS jobTitle,
          JOB_COMPANY_NAME    AS jobCompanyName,
          JOB_DETAIL_URL      AS jobDetailUrl,
          JOB_DDAY            AS jobDday,
          JOB_DEADLINE        AS jobDeadline,
          JOB_SALARY_PREVIEW  AS jobSalaryPreview,
          JOB_POSITION        AS jobPosition,
          JOB_RECRUIT_COUNT   AS jobRecruitCount,
          JOB_EXPERIENCE      AS jobExperience,
          JOB_EDUCATION       AS jobEducation,
          JOB_EMPLOYMENT_TYPE AS jobEmploymentType,
          JOB_WORK_LOCATION   AS jobWorkLocation,
          JOB_SALARY          AS jobSalary,
          JOB_RANK            AS jobRank,
          JOB_PREFERENCE      AS jobPreference,
          JOB_WORK_TYPE       AS jobWorkType,
          JOB_WORK_HOURS      AS jobWorkHours,
          JOB_NEARBY_STATION  AS jobNearbyStation,
          CREATED_AT          AS createdAt,
          UPDATED_AT          AS updatedAt
        FROM JOB_INFO
        WHERE JOB_NO = #{jobNo}
        """)
    Job findByJobNo(Integer jobNo);

    /**
     * 최근 채용공고 10건 조회
     */
    @Select("""
        SELECT
          JOB_NO              AS jobNo,
          USR_ID              AS usrId,
          JOB_TITLE           AS jobTitle,
          JOB_COMPANY_NAME    AS jobCompanyName,
          JOB_DETAIL_URL      AS jobDetailUrl,
          JOB_DDAY            AS jobDday,
          JOB_DEADLINE        AS jobDeadline,
          JOB_SALARY_PREVIEW  AS jobSalaryPreview,
          JOB_POSITION        AS jobPosition,
          JOB_RECRUIT_COUNT   AS jobRecruitCount,
          JOB_EXPERIENCE      AS jobExperience,
          JOB_EDUCATION       AS jobEducation,
          JOB_EMPLOYMENT_TYPE AS jobEmploymentType,
          JOB_WORK_LOCATION   AS jobWorkLocation,
          JOB_SALARY          AS jobSalary,
          JOB_RANK            AS jobRank,
          JOB_PREFERENCE      AS jobPreference,
          JOB_WORK_TYPE       AS jobWorkType,
          JOB_WORK_HOURS      AS jobWorkHours,
          JOB_NEARBY_STATION  AS jobNearbyStation,
          CREATED_AT          AS createdAt,
          UPDATED_AT          AS updatedAt
        FROM JOB_INFO
        ORDER BY CREATED_AT DESC
        LIMIT 10
        """)
    List<Job> selectJobList();
    
    /**
     * 채용공고 정보 저장
     */
    @Insert("""
        INSERT INTO JOB_INFO (
            USR_ID, JOB_TITLE, JOB_COMPANY_NAME, JOB_DETAIL_URL, JOB_DDAY, JOB_DEADLINE, JOB_SALARY_PREVIEW,
            JOB_POSITION, JOB_RECRUIT_COUNT, JOB_EXPERIENCE, JOB_EDUCATION,
            JOB_EMPLOYMENT_TYPE, JOB_WORK_LOCATION, JOB_SALARY, JOB_RANK,
            JOB_PREFERENCE, JOB_WORK_TYPE, JOB_WORK_HOURS, JOB_NEARBY_STATION
        ) VALUES (
            #{usrId}, #{jobTitle}, #{jobCompanyName}, #{jobDetailUrl}, #{jobDday}, #{jobDeadline}, #{jobSalaryPreview},
            #{jobPosition}, #{jobRecruitCount}, #{jobExperience}, #{jobEducation},
            #{jobEmploymentType}, #{jobWorkLocation}, #{jobSalary}, #{jobRank},
            #{jobPreference}, #{jobWorkType}, #{jobWorkHours}, #{jobNearbyStation}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "jobNo")
    void insertJob(Job job);
    
    /**
     * 마감일이 지난 채용공고 삭제
     */
    @Delete("""
        DELETE FROM JOB_INFO
        WHERE JOB_DEADLINE < #{today}
        """)
    int deleteExpiredJobs(@Param("today") LocalDate today);
    
    /**
     * 중복 체크 (기업명 + 채용공고 제목)
     */
    @Select("""
        SELECT COUNT(*) 
        FROM JOB_INFO 
        WHERE JOB_COMPANY_NAME = #{companyName} 
        AND JOB_TITLE = #{jobTitle}
        """)
    int countByCompanyAndTitle(@Param("companyName") String companyName, @Param("jobTitle") String jobTitle);
}

