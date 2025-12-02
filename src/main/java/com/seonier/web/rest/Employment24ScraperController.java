package com.seonier.web.rest;

import com.seonier.service.Employment24ScraperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 고용24 스크래핑 테스트용 REST 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/scraper")
@RequiredArgsConstructor
public class Employment24ScraperController {

    private final Employment24ScraperService scraperService;
    
    /**
     * 수동으로 스크래핑 실행
     * GET/POST http://localhost:8080/api/scraper/run
     */
    @GetMapping("/run")
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runScraper() {
        log.info("수동 스크래핑 요청 수신");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            scraperService.scrapeAndSaveJobs();
            
            response.put("success", true);
            response.put("message", "스크래핑 완료");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("스크래핑 실행 중 오류", e);
            
            response.put("success", false);
            response.put("message", "스크래핑 실패: " + e.getMessage());
            response.put("count", 0);
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
