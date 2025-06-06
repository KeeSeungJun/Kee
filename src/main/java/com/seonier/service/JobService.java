package com.seonier.service;

import com.seonier.persistence.model.Job;
import com.seonier.persistence.mapper.JobMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    private final JobMapper jobMapper;

    public JobService(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    /** 모든 일자리 조회 */
    public List<Job> findAll() {
        return jobMapper.findAll();
    }

    /** 단일 일자리 조회 */
    public Job findByJobNo(Long jobNo) {
        return jobMapper.findByJobNo(jobNo);
    }
}
