package com.seonier.web.rest.controller;

import com.seonier.dto.response.DefaultResponse;
import com.seonier.dto.response.JobCategoryResponse;
import com.seonier.service.JobCategoryService;
import com.seonier.web.lang.AbstractController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 직업분류 API Controller
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jobs")
public class JobCategoryController extends AbstractController {

    private final JobCategoryService jobCategoryService;

    /**
     * 전체 직업분류 목록 조회
     *
     * @return 직업분류 목록
     */
    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse getAllJobCategories() {
        log.debug("Get all job categories");
        List<JobCategoryResponse> categories = jobCategoryService.getAllJobCategories();
        return DefaultResponse.builder()
                .put("categories", categories)
                .put("total", categories.size())
                .build();
    }

    /**
     * 대분류별 직업 목록 조회
     *
     * @param majorCode 대분류 코드
     * @return 직업 목록
     */
    @GetMapping(path = "/categories/major/{majorCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse getJobsByMajorCode(@PathVariable String majorCode) {
        log.debug("Get jobs by major code: {}", majorCode);
        List<JobCategoryResponse> jobs = jobCategoryService.getJobsByMajorCode(majorCode);
        return DefaultResponse.builder()
                .put("jobs", jobs)
                .put("total", jobs.size())
                .build();
    }

    /**
     * 중분류별 직업 목록 조회
     *
     * @param middleCode 중분류 코드
     * @return 직업 목록
     */
    @GetMapping(path = "/categories/middle/{middleCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse getJobsByMiddleCode(@PathVariable String middleCode) {
        log.debug("Get jobs by middle code: {}", middleCode);
        List<JobCategoryResponse> jobs = jobCategoryService.getJobsByMiddleCode(middleCode);
        return DefaultResponse.builder()
                .put("jobs", jobs)
                .put("total", jobs.size())
                .build();
    }

    /**
     * 직업 코드로 직업 정보 조회
     *
     * @param jobCode 직업 코드
     * @return 직업 정보
     */
    @GetMapping(path = "/categories/{jobCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse getJobByCode(@PathVariable String jobCode) {
        log.debug("Get job by code: {}", jobCode);
        JobCategoryResponse job = jobCategoryService.getJobByCode(jobCode);
        return DefaultResponse.builder()
                .put("job", job)
                .build();
    }

    /**
     * 직업명으로 직업 정보 검색
     *
     * @param jobName 직업명 (부분 일치)
     * @return 직업 목록
     */
    @GetMapping(path = "/categories/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse searchJobsByName(@RequestParam String jobName) {
        log.debug("Search jobs by name: {}", jobName);
        List<JobCategoryResponse> jobs = jobCategoryService.searchJobsByName(jobName);
        return DefaultResponse.builder()
                .put("jobs", jobs)
                .put("total", jobs.size())
                .build();
    }

}
