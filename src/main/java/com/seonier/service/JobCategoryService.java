package com.seonier.service;

import com.seonier.dto.response.JobCategoryResponse;
import com.seonier.persistence.mapper.JobCategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 직업분류 서비스
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JobCategoryService extends AbstractService {

    private final JobCategoryMapper jobCategoryMapper;

    /**
     * 전체 직업분류 목록 조회
     *
     * @return 직업분류 목록
     */
    public List<JobCategoryResponse> getAllJobCategories() {
        log.debug("Get all job categories");
        return jobCategoryMapper.findAllJobCategories();
    }

    /**
     * 대분류별 직업 목록 조회
     *
     * @param majorCode 대분류 코드
     * @return 직업 목록
     */
    public List<JobCategoryResponse> getJobsByMajorCode(String majorCode) {
        log.debug("Get jobs by major code: {}", majorCode);
        return jobCategoryMapper.findJobsByMajorCode(majorCode);
    }

    /**
     * 중분류별 직업 목록 조회
     *
     * @param middleCode 중분류 코드
     * @return 직업 목록
     */
    public List<JobCategoryResponse> getJobsByMiddleCode(String middleCode) {
        log.debug("Get jobs by middle code: {}", middleCode);
        return jobCategoryMapper.findJobsByMiddleCode(middleCode);
    }

    /**
     * 직업 코드로 직업 정보 조회
     *
     * @param jobCode 직업 코드
     * @return 직업 정보
     */
    public JobCategoryResponse getJobByCode(String jobCode) {
        log.debug("Get job by code: {}", jobCode);
        return jobCategoryMapper.findJobByCode(jobCode);
    }

    /**
     * 직업명으로 직업 정보 검색
     *
     * @param jobName 직업명 (부분 일치)
     * @return 직업 목록
     */
    public List<JobCategoryResponse> searchJobsByName(String jobName) {
        log.debug("Search jobs by name: {}", jobName);
        return jobCategoryMapper.searchJobsByName(jobName);
    }

}
