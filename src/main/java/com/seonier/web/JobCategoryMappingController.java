package com.seonier.web;

import com.seonier.dto.response.DefaultResponse;
import com.seonier.service.JobCategoryMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 직업 분류 매핑 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/job-mapping")
@RequiredArgsConstructor
public class JobCategoryMappingController {

    private final JobCategoryMappingService jobCategoryMappingService;

    /**
     * 직업 분류 매핑 실행
     * 1단계: 유사 매칭
     * 2단계: OpenAI 매핑
     */
    @PostMapping("/execute")
    public ResponseEntity<DefaultResponse> executeMapping() {
        try {
            log.info("직업 분류 매핑 실행 요청");
            
            jobCategoryMappingService.mapAllJobCategories();
            
            return ResponseEntity.ok(DefaultResponse.builder()
                    .message("직업 분류 매핑 완료")
                    .build());
                    
        } catch (Exception e) {
            log.error("직업 분류 매핑 실패", e);
            return ResponseEntity.internalServerError()
                    .body(DefaultResponse.builder()
                            .message("매핑 실패: " + e.getMessage())
                            .build());
        }
    }
}
