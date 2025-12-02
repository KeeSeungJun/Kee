package com.seonier.service;

import com.seonier.persistence.mapper.QnaMapper;
import com.seonier.persistence.model.Qna;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class QnaService {

    private final QnaMapper qnaMapper;

    public QnaService(QnaMapper qnaMapper) {
        this.qnaMapper = qnaMapper;
    }

    /**
     * 전체 QnA 목록 조회
     */
    public List<Qna> getAllQnas() {
        log.debug("Get all QnAs");
        return qnaMapper.findAll();
    }

    /**
     * QnA 번호로 조회
     */
    public Qna getQnaByNo(int qnaNo) {
        log.debug("Get QnA by qnaNo: {}", qnaNo);
        return qnaMapper.findByQnaNo(qnaNo);
    }

    /**
     * QnA 등록 (사용자)
     */
    @Transactional
    public boolean insertQna(Qna qna) {
        log.debug("Insert QnA: {}", qna);
        try {
            qnaMapper.insertQna(qna);
            return true;
        } catch (Exception e) {
            log.error("Failed to insert QnA", e);
            return false;
        }
    }

    /**
     * 답변 등록/수정
     */
    @Transactional
    public boolean updateAnswer(Qna qna) {
        log.debug("Update QnA answer: {}", qna);
        try {
            qnaMapper.updateAnswer(qna);
            return true;
        } catch (Exception e) {
            log.error("Failed to update QnA answer", e);
            return false;
        }
    }

    /**
     * QnA 삭제
     */
    @Transactional
    public boolean deleteQna(int qnaNo) {
        log.debug("Delete QnA: {}", qnaNo);
        try {
            qnaMapper.deleteQna(qnaNo);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete QnA", e);
            return false;
        }
    }

    /**
     * 검색어로 QnA 조회
     */
    public List<Qna> searchQna(String keyword) {
        log.debug("Search QnA with keyword: {}", keyword);
        return qnaMapper.searchQna(keyword);
    }

    /**
     * 상태별 QnA 조회
     */
    public List<Qna> getQnasByStatus(String status) {
        log.debug("Get QnAs by status: {}", status);
        return qnaMapper.findByStatus(status);
    }
}
