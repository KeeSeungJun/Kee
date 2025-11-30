package com.seonier.persistence.mapper;

import com.seonier.dto.response.JobCategoryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 직업분류 Mapper
 *
 * @version 1.0.0
 */
@Mapper
public interface JobCategoryMapper {

    /**
     * 전체 직업분류 목록 조회
     *
     * @return 직업분류 목록
     */
    List<JobCategoryResponse> findAllJobCategories();

    /**
     * 대분류별 직업 목록 조회
     *
     * @param majorCode 대분류 코드
     * @return 직업 목록
     */
    List<JobCategoryResponse> findJobsByMajorCode(String majorCode);

    /**
     * 중분류별 직업 목록 조회
     *
     * @param middleCode 중분류 코드
     * @return 직업 목록
     */
    List<JobCategoryResponse> findJobsByMiddleCode(String middleCode);

    /**
     * 직업 코드로 직업 정보 조회
     *
     * @param jobCode 직업 코드
     * @return 직업 정보
     */
    JobCategoryResponse findJobByCode(String jobCode);

    /**
     * 직업명으로 직업 정보 검색
     *
     * @param jobName 직업명 (부분 일치)
     * @return 직업 목록
     */
    List<JobCategoryResponse> searchJobsByName(String jobName);

}
