package com.seonier.persistence.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JOB_INFO 테이블 매핑 엔티티
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("JOB_INFO")
public class Job {

    @Id
    private Integer jobNo;                      // JOB_NO

    private String usrId;                       // USR_ID
    private String jobTitle;                    // JOB_TITLE
    private String jobTask;                     // JOB_TASK
    private String jobDesc;                     // JOB_DESC
    private String jobLicenseRequired;          // JOB_LICENSE_REQUIRED
    private String jobContractPeriod;           // JOB_CONTRACT_PERIOD
    private String jobSalary;                   // JOB_SALARY
    private String jobWorkHours;                // JOB_WORK_HOURS
    private String jobBreakTime;                // JOB_BREAK_TIME
    private String jobBenefits;                 // JOB_BENEFITS
    private String jobAddress;                  // JOB_ADDRESS
    private Double jobLatitude;                 // JOB_LATITUDE
    private Double jobLongitude;                // JOB_LONGITUDE
    private String jobNearbySubway;             // JOB_NEARBY_SUBWAY
    private String jobBusRoutes;                // JOB_BUS_ROUTES

    private LocalDateTime createdAt;            // CREATED_AT
    private LocalDateTime updatedAt;            // UPDATED_AT

}
