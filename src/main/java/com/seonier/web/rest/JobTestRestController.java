package com.seonier.web.rest;

import com.seonier.persistence.model.Job;
import com.seonier.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 디버그용 REST 엔드포인트: 템플릿 렌더링 문제 분리 진단용
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JobTestRestController {
    private final JobService jobService;

    @GetMapping("/jobs-test")
    public ResponseEntity<List<Job>> jobsTest() {
        List<Job> jobs = jobService.selectJobList();
        return ResponseEntity.ok(jobs);
    }
}
