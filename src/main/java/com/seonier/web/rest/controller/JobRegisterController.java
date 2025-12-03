package com.seonier.web.rest.controller;

import com.seonier.dto.request.JobRegisterRequest;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.mapper.JobMapper;
import com.seonier.persistence.model.Job;
import com.seonier.web.lang.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 일자리 등록 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobRegisterController extends AbstractController {

    private final JobMapper jobMapper;

    /**
     * 일자리 수동 등록
     */
    @PostMapping("/register")
    public ResponseEntity<DefaultResponse> registerJob(
            HttpServletRequest request,
            @RequestBody JobRegisterRequest params
    ) {
        try {
            String userId = getUserIdFromCookies(request);
            log.info("일자리 등록 요청 - 사용자: {}, 업무명: {}", userId, params.getJobTitle());
            
            // Job 엔티티 생성
            Job job = new Job();
            job.setUsrId(userId);
            job.setJobTitle(params.getJobTitle());
            job.setJobCompanyName(params.getCompanyName());
            job.setJobWorkLocation(params.getLocation());
            job.setJobSalary(params.getSalary());
            job.setJobPosition(params.getJobTitle()); // 업무명을 직종으로도 설정
            
            // 기본값 설정
            job.setJobSalaryPreview(params.getSalary());
            job.setJobDetailUrl("-");
            job.setJobDday("상시모집");
            job.setJobDeadline(LocalDate.now().plusMonths(1)); // 1개월 후
            job.setJobRecruitCount("-");
            job.setJobExperience("무관");
            job.setJobEducation("무관");
            job.setJobEmploymentType("정규직");
            job.setJobRank("-");
            job.setJobPreference("-");
            job.setJobWorkType("-");
            job.setJobWorkHours("-");
            job.setJobNearbyStation("-");
            
            // DB 저장
            jobMapper.insertJob(job);
            log.info("일자리 등록 완료 - JOB_NO: {}", job.getJobNo());
            
            return ResponseEntity.ok(DefaultResponse.builder()
                    .message("일자리가 성공적으로 등록되었습니다.")
                    .put("jobNo", job.getJobNo())
                    .build());
                    
        } catch (Exception e) {
            log.error("일자리 등록 실패", e);
            return ResponseEntity.internalServerError()
                    .body(DefaultResponse.builder()
                            .message("등록 실패: " + e.getMessage())
                            .build());
        }
    }
}
