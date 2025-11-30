package com.seonier.web.rest;

import com.seonier.persistence.mapper.JobMapper;
import com.seonier.persistence.model.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 채용공고 데이터 확인용 컨트롤러
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobCheckController {

    private final JobMapper jobMapper;

    /**
     * 전체 채용공고 개수 조회
     * GET http://localhost:8080/api/jobs/count
     */
    @GetMapping("/count")
    public Map<String, Object> getJobCount() {
        List<Job> allJobs = jobMapper.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", allJobs.size());
        result.put("message", "총 " + allJobs.size() + "건의 채용공고가 저장되어 있습니다.");
        return result;
    }

    /**
     * 최근 채용공고 10건 조회
     * GET http://localhost:8080/api/jobs/recent
     */
    @GetMapping("/recent")
    public Map<String, Object> getRecentJobs() {
        List<Job> recentJobs = jobMapper.selectJobList();
        Map<String, Object> result = new HashMap<>();
        result.put("count", recentJobs.size());
        result.put("jobs", recentJobs);
        return result;
    }

    /**
     * 전체 채용공고 조회 (주의: 데이터가 많으면 느릴 수 있음)
     * GET http://localhost:8080/api/jobs/all
     */
    @GetMapping("/all")
    public Map<String, Object> getAllJobs() {
        List<Job> allJobs = jobMapper.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", allJobs.size());
        result.put("jobs", allJobs);
        return result;
    }

    /**
     * 채용공고 샘플 1건 조회
     * GET http://localhost:8080/api/jobs/sample
     */
    @GetMapping("/sample")
    public Map<String, Object> getSampleJob() {
        List<Job> jobs = jobMapper.selectJobList();
        Map<String, Object> result = new HashMap<>();
        if (!jobs.isEmpty()) {
            result.put("sample", jobs.get(0));
            result.put("message", "샘플 데이터입니다.");
        } else {
            result.put("message", "저장된 채용공고가 없습니다.");
        }
        return result;
    }
}
