package com.seonier.web.rest.controller;

import com.seonier.dto.response.DefaultResponse;
import com.seonier.service.JobLocationUpdateService;
import com.seonier.web.lang.AbstractController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 일자리 위치 정보 업데이트 컨트롤러 (관리자용)
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/job-location")
public class JobLocationController extends AbstractController {

    private final JobLocationUpdateService jobLocationUpdateService;

    /**
     * 모든 일자리의 위치 정보 업데이트
     * (주소 → 위도/경도 변환)
     * 
     * GET /api/admin/job-location/update-all
     */
    @GetMapping("/update-all")
    public ResponseEntity<DefaultResponse> updateAllJobLocations() {
        log.info("=== 전체 일자리 위치 정보 업데이트 시작 ===");
        
        try {
            // 비동기 실행 (백그라운드에서 처리)
            new Thread(() -> {
                jobLocationUpdateService.updateAllJobLocations();
            }).start();
            
            return ResponseEntity.ok(DefaultResponse.builder()
                    .code(HttpStatus.OK.value())
                    .message("일자리 위치 정보 업데이트를 시작했습니다. 백그라운드에서 처리됩니다.")
                    .build());
        } catch (Exception e) {
            log.error("일자리 위치 정보 업데이트 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DefaultResponse.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("업데이트 중 오류가 발생했습니다: " + e.getMessage())
                            .build());
        }
    }

    /**
     * 특정 일자리의 위치 정보 업데이트
     * 
     * GET /api/admin/job-location/update/{jobNo}
     */
    @GetMapping("/update/{jobNo}")
    public ResponseEntity<DefaultResponse> updateJobLocation(@PathVariable Integer jobNo) {
        log.info("=== 일자리 위치 정보 업데이트: jobNo={} ===", jobNo);
        
        try {
            boolean success = jobLocationUpdateService.updateJobLocation(jobNo);
            
            if (success) {
                return ResponseEntity.ok(DefaultResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message("일자리 위치 정보 업데이트 성공")
                        .build());
            } else {
                return ResponseEntity.ok(DefaultResponse.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message("일자리를 찾을 수 없습니다: jobNo=" + jobNo)
                        .build());
            }
        } catch (Exception e) {
            log.error("일자리 위치 정보 업데이트 실패: jobNo={}", jobNo, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DefaultResponse.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("업데이트 중 오류가 발생했습니다: " + e.getMessage())
                            .build());
        }
    }
}
