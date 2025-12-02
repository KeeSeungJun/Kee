package com.seonier.service;

import com.seonier.persistence.mapper.FaqMapper;
import com.seonier.persistence.model.Faq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FaqService {
    
    private final FaqMapper faqMapper;

    public FaqService(FaqMapper faqMapper) {
        this.faqMapper = faqMapper;
    }

    /**
     * 전체 FAQ 목록 조회
     */
    public List<Faq> getAllFaqs() {
        return faqMapper.findAll();
    }

    /**
     * FAQ 번호로 조회
     */
    public Faq getFaqByNo(int faqNo) {
        return faqMapper.findByFaqNo(faqNo);
    }

    /**
     * FAQ 등록
     */
    public boolean insertFaq(Faq faq) {
        try {
            faqMapper.insertFaq(faq);
            return true;
        } catch (Exception e) {
            log.error("FAQ 등록 실패", e);
            return false;
        }
    }

    /**
     * FAQ 수정
     */
    public boolean updateFaq(Faq faq) {
        try {
            faqMapper.updateFaq(faq);
            return true;
        } catch (Exception e) {
            log.error("FAQ 수정 실패", e);
            return false;
        }
    }

    /**
     * FAQ 삭제
     */
    public boolean deleteFaq(int faqNo) {
        try {
            faqMapper.deleteFaq(faqNo);
            return true;
        } catch (Exception e) {
            log.error("FAQ 삭제 실패", e);
            return false;
        }
    }

    /**
     * 검색어로 FAQ 조회
     */
    public List<Faq> searchFaq(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllFaqs();
        }
        return faqMapper.searchFaq(keyword.trim());
    }
}
