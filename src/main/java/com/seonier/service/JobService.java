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
        return jobMapper.findByJobNo(jobNo.intValue());
    }

    public List<Job> selectJobList() {
        return jobMapper.selectJobList();
    }

    public Job findById(Integer jobNo) {
        return jobMapper.findByJobNo(jobNo);
    }

    public void updateJobLocation(Job job) {
        jobMapper.updateJobLocation(job);
    }

    /**
     * 전체 일자리 목록 조회 (관리자용)
     */
    public List<Job> getAllJobs() {
        return jobMapper.findAll();
    }

    /**
     * 일자리 번호로 조회
     */
    public Job getJobByNo(Long jobNo) {
        return jobMapper.findByJobNo(jobNo.intValue());
    }

    /**
     * 일자리 수정
     */
    public boolean updateJob(Job job) {
        try {
            jobMapper.updateJob(job);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 일자리 삭제
     */
    public boolean deleteJob(Long jobNo) {
        try {
            jobMapper.deleteJob(jobNo.intValue());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 일자리 등록
     */
    public boolean insertJob(Job job) {
        try {
            jobMapper.insertJob(job);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
