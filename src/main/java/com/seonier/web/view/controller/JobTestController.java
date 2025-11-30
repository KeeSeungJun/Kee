package com.seonier.web.view.controller;

import com.seonier.persistence.model.Job;
import com.seonier.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class JobTestController {

    private final JobService jobService;

    /**
     * /jobs-test 경로로 요청 시 RDS의 JOB_INFO 데이터를 조회하여 화면에 출력
     * @param model 뷰에 데이터를 담기 위한 Model 객체
     * @return jobs-test.html 템플릿 경로
     */
    @GetMapping("/jobs-test")
    public String jobsTest(Model model) {
        log.debug("Access the Jobs Test page.");
        
        try {
            List<Job> jobList = jobService.selectJobList();
            log.info("Successfully retrieved {} jobs from database", jobList.size());
            
            model.addAttribute("jobList", jobList);
            model.addAttribute("totalCount", jobList.size());
            model.addAttribute("errorMessage", null);
            
            return "view/jobs-test";
        } catch (Exception e) {
            log.error("Error occurred while retrieving job list", e);
            model.addAttribute("jobList", null);
            model.addAttribute("totalCount", 0);
            model.addAttribute("errorMessage", "데이터를 조회하는 중 오류가 발생했습니다: " + e.getMessage());
            return "view/jobs-test";
        }
    }
}
