package com.seonier.web.rest.controller;

import com.seonier.persistence.model.Qna;
import com.seonier.service.QnaService;
import com.seonier.web.lang.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/qnas")
public class QnaController extends AbstractController {

    private final QnaService qnaService;

    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    /**
     * 전체 QnA 목록 조회 (검색 및 상태 필터 포함)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllQnas(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        log.debug("Get all QnAs. keyword={}, status={}", keyword, status);
        
        List<Qna> qnas;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            qnas = qnaService.searchQna(keyword);
        } else if (status != null && !status.equals("all")) {
            qnas = qnaService.getQnasByStatus(status.toUpperCase());
        } else {
            qnas = qnaService.getAllQnas();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("qnas", qnas);
        response.put("total", qnas.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 QnA 조회
     */
    @GetMapping("/{qnaNo}")
    public ResponseEntity<Qna> getQna(@PathVariable int qnaNo) {
        log.debug("Get QnA. qnaNo={}", qnaNo);
        
        Qna qna = qnaService.getQnaByNo(qnaNo);
        if (qna == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(qna);
    }

    /**
     * QnA 등록 (사용자)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createQna(@RequestBody Qna qna) {
        log.debug("Create QnA. qna={}", qna);
        
        boolean success = qnaService.insertQna(qna);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "문의가 등록되었습니다." : "문의 등록에 실패했습니다.");
        if (success) {
            response.put("qnaNo", qna.getQnaNo());
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * 답변 등록/수정 (관리자)
     */
    @PutMapping("/{qnaNo}/answer")
    public ResponseEntity<Map<String, Object>> updateAnswer(
            @PathVariable int qnaNo,
            @RequestBody Map<String, String> request
    ) {
        log.debug("Update QnA answer. qnaNo={}, request={}", qnaNo, request);
        
        Qna qna = new Qna();
        qna.setQnaNo(qnaNo);
        qna.setQnaAnswer(request.get("answer"));
        qna.setAnswerBy(request.getOrDefault("answerBy", "admin"));
        
        boolean success = qnaService.updateAnswer(qna);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "답변이 등록되었습니다." : "답변 등록에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * QnA 삭제
     */
    @DeleteMapping("/{qnaNo}")
    public ResponseEntity<Map<String, Object>> deleteQna(@PathVariable int qnaNo) {
        log.debug("Delete QnA. qnaNo={}", qnaNo);
        
        boolean success = qnaService.deleteQna(qnaNo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "삭제되었습니다." : "삭제에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }
}
