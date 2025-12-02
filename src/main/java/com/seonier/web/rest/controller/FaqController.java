package com.seonier.web.rest.controller;

import com.seonier.persistence.model.Faq;
import com.seonier.service.FaqService;
import com.seonier.web.lang.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/faqs")
public class FaqController extends AbstractController {

    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    /**
     * 전체 FAQ 목록 조회
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFaqs(
            @RequestParam(required = false) String keyword
    ) {
        log.debug("Get all FAQs. keyword={}", keyword);
        
        List<Faq> faqs = keyword != null && !keyword.trim().isEmpty()
                ? faqService.searchFaq(keyword)
                : faqService.getAllFaqs();
        
        Map<String, Object> response = new HashMap<>();
        response.put("faqs", faqs);
        response.put("total", faqs.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 FAQ 조회
     */
    @GetMapping("/{faqNo}")
    public ResponseEntity<Faq> getFaq(@PathVariable int faqNo) {
        log.debug("Get FAQ. faqNo={}", faqNo);
        
        Faq faq = faqService.getFaqByNo(faqNo);
        if (faq == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(faq);
    }

    /**
     * FAQ 등록
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createFaq(@RequestBody Faq faq) {
        log.debug("Create FAQ. faq={}", faq);
        
        // 작성자 ID 설정 (현재 로그인한 사용자 ID 또는 'admin')
        if (faq.getCreatedBy() == null) {
            faq.setCreatedBy("admin");
        }
        
        boolean success = faqService.insertFaq(faq);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "등록되었습니다." : "등록에 실패했습니다.");
        if (success) {
            response.put("faqNo", faq.getFaqNo());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * FAQ 수정
     */
    @PutMapping("/{faqNo}")
    public ResponseEntity<Map<String, Object>> updateFaq(
            @PathVariable int faqNo,
            @RequestBody Faq faq
    ) {
        log.debug("Update FAQ. faqNo={}, faq={}", faqNo, faq);
        
        faq.setFaqNo(faqNo);
        boolean success = faqService.updateFaq(faq);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "수정되었습니다." : "수정에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * FAQ 삭제
     */
    @DeleteMapping("/{faqNo}")
    public ResponseEntity<Map<String, Object>> deleteFaq(@PathVariable int faqNo) {
        log.debug("Delete FAQ. faqNo={}", faqNo);
        
        boolean success = faqService.deleteFaq(faqNo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "삭제되었습니다." : "삭제에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }
}
