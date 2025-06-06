package com.seonier.persistence.mapper;

import com.seonier.persistence.model.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobMapper {
    @Select("""
        SELECT
          JOB_NO        AS jobNo,
          USR_ID        AS usrId,
          JOB_TITLE     AS jobTitle,
          JOB_TASK      AS jobTask,
          JOB_DESC      AS jobDesc,
          JOB_LICENSE_REQUIRED AS jobLicenseRequired,
          JOB_CONTRACT_PERIOD  AS jobContractPeriod,
          JOB_SALARY    AS jobSalary,
          JOB_WORK_HOURS       AS jobWorkHours,
          JOB_BREAK_TIME       AS jobBreakTime,
          JOB_BENEFITS         AS jobBenefits,
          JOB_ADDRESS          AS jobAddress,
          JOB_LATITUDE         AS jobLatitude,
          JOB_LONGITUDE        AS jobLongitude,
          JOB_NEARBY_SUBWAY    AS jobNearbySubway,
          JOB_BUS_ROUTES       AS jobBusRoutes,
          CREATED_AT   AS createdAt,
          UPDATED_AT   AS updatedAt
        FROM JOB_INFO
        """)
    List<Job> findAll();

    @Select("SELECT * FROM JOB_INFO WHERE JOB_NO = #{jobNo}")
    Job findByJobNo(Long jobNo);
}
