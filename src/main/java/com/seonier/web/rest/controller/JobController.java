package com.seonier.web.rest.controller;

import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.model.Job;
import com.seonier.service.JobService;
import com.seonier.service.OpenAiService;
import com.seonier.web.lang.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JobController extends AbstractController {


    private final OpenAiService openaiService;

    private final JobService jobService;

    @GetMapping("/api/job")
    public DefaultResponse job(HttpServletRequest request) throws IOException {
        String userId = getUserIdFromCookies(request);
        log.debug("userId: {}", userId);
        DefaultResponse promptResponse = openaiService.getPrompt(userId);
        log.debug("OpenAI getPrompt() 반환값: {}", promptResponse);
        return promptResponse;
    }


//    /** 전체 일자리 목록을 JSON으로 반환 */
//    @GetMapping("/api/jobs")
//    public DefaultResponse getAllJobs() {
//        List<Job> jobs = jobService.findAll();
//        return DefaultResponse.builder()
//                .put("jobs", jobs)
//                .build();
//    }
}

