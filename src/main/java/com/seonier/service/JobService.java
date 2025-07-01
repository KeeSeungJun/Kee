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


    public List<Job> findAll() {
        return jobMapper.findAll();
    }


    public Job findByJobNo(Long jobNo) {
        return jobMapper.findByJobNo(jobNo);
    }
}
