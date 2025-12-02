package com.seonier.web.rest.controller;

import com.seonier.persistence.model.Job;
import com.seonier.service.JobService;
import com.seonier.web.lang.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/jobs")
public class JobInfoController extends AbstractController {

    private final JobService jobService;

    public JobInfoController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * 전체 일자리 목록 조회 (관리자용)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllJobs(
            @RequestParam(required = false) String keyword
    ) {
        log.debug("Get all jobs. keyword={}", keyword);
        
        List<Job> jobs = jobService.getAllJobs();
        
        // 검색어 필터링
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim().toLowerCase();
            jobs = jobs.stream()
                    .filter(job -> 
                        (job.getJobTitle() != null && job.getJobTitle().toLowerCase().contains(kw)) ||
                        (job.getJobWorkLocation() != null && job.getJobWorkLocation().toLowerCase().contains(kw)) ||
                        (job.getJobCompanyName() != null && job.getJobCompanyName().toLowerCase().contains(kw))
                    )
                    .toList();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobs);
        response.put("total", jobs.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 일자리 조회
     */
    @GetMapping("/{jobNo}")
    public ResponseEntity<Job> getJob(@PathVariable Long jobNo) {
        log.debug("Get job. jobNo={}", jobNo);
        
        Job job = jobService.getJobByNo(jobNo);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(job);
    }

    /**
     * 일자리 수정
     */
    @PutMapping("/{jobNo}")
    public ResponseEntity<Map<String, Object>> updateJob(
            @PathVariable Long jobNo,
            @RequestBody Job job
    ) {
        log.debug("Update job. jobNo={}, job={}", jobNo, job);
        
        job.setJobNo(jobNo);
        boolean success = jobService.updateJob(job);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "수정되었습니다." : "수정에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 일자리 삭제
     */
    @DeleteMapping("/{jobNo}")
    public ResponseEntity<Map<String, Object>> deleteJob(@PathVariable Long jobNo) {
        log.debug("Delete job. jobNo={}", jobNo);
        
        boolean success = jobService.deleteJob(jobNo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "삭제되었습니다." : "삭제에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 일자리 등록
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createJob(@RequestBody Job job) {
        log.debug("Create job. job={}", job);
        
        // 등록자 아이디 설정 (현재 로그인한 사용자 ID 또는 'ADMIN')
        if (job.getUserId() == null) {
            job.setUserId("ADMIN");
        }
        
        boolean success = jobService.insertJob(job);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "등록되었습니다." : "등록에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }
}
