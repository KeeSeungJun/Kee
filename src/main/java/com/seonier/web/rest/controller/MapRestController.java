package com.seonier.web.rest.controller;

import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.mapper.JobMapper;
import com.seonier.persistence.model.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 지도 관련 REST API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapRestController {

    private final JobMapper jobMapper;

    /**
     * 테스트 엔드포인트
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("테스트 API 호출됨");
        return ResponseEntity.ok("MapRestController is working!");
    }

    /**
     * 위치 정보가 있는 모든 일자리 조회
     */
    @GetMapping("/jobs")
    public ResponseEntity<DefaultResponse> getJobsWithLocation() {
        try {
            log.info("일자리 조회 API 호출됨");
            List<Job> jobs = jobMapper.findJobsWithLocation();
            log.info("위치 정보가 있는 일자리 {}건 조회 완료", jobs.size());
            
            if (jobs.isEmpty()) {
                log.warn("위치 정보가 있는 일자리가 0건입니다.");
            }
            
            return ResponseEntity.ok(DefaultResponse.builder()
                    .message("조회 성공")
                    .put("data", jobs)
                    .build());
                    
        } catch (Exception e) {
            log.error("일자리 조회 실패", e);
            log.error("에러 상세: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(DefaultResponse.builder()
                            .message("조회 실패: " + e.getMessage())
                            .build());
        }
    }
}
