package com.seonier.persistence.mapper;

import com.seonier.persistence.model.Faq;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FaqMapper {

    /**
     * 전체 FAQ 목록 조회
     */
    @Select("""
        SELECT 
            FAQ_NO as faqNo,
            FAQ_CATEGORY as faqCategory,
            FAQ_QUESTION as faqQuestion,
            FAQ_ANSWER as faqAnswer,
            CREATED_BY as createdBy,
            CREATED_AT as createdAt,
            UPDATED_AT as updatedAt
        FROM FAQ_INFO
        WHERE USE_YN = 'Y'
        ORDER BY FAQ_ORDER, CREATED_AT DESC
        """)
    List<Faq> findAll();

    /**
     * FAQ 번호로 조회
     */
    @Select("""
        SELECT 
            FAQ_NO as faqNo,
            FAQ_CATEGORY as faqCategory,
            FAQ_QUESTION as faqQuestion,
            FAQ_ANSWER as faqAnswer,
            CREATED_BY as createdBy,
            CREATED_AT as createdAt,
            UPDATED_AT as updatedAt
        FROM FAQ_INFO
        WHERE FAQ_NO = #{faqNo}
        """)
    Faq findByFaqNo(int faqNo);

    /**
     * FAQ 등록
     */
    @Insert("""
        INSERT INTO FAQ_INFO (
            FAQ_CATEGORY, FAQ_QUESTION, FAQ_ANSWER, CREATED_BY, USE_YN
        ) VALUES (
            #{faqCategory}, #{faqQuestion}, #{faqAnswer}, #{createdBy}, 'Y'
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "faqNo")
    void insertFaq(Faq faq);

    /**
     * FAQ 수정
     */
    @Update("""
        UPDATE FAQ_INFO
        SET FAQ_CATEGORY = #{faqCategory},
            FAQ_QUESTION = #{faqQuestion},
            FAQ_ANSWER = #{faqAnswer},
            UPDATED_AT = CURRENT_TIMESTAMP
        WHERE FAQ_NO = #{faqNo}
        """)
    void updateFaq(Faq faq);

    /**
     * FAQ 삭제
     */
    @Delete("DELETE FROM FAQ_INFO WHERE FAQ_NO = #{faqNo}")
    void deleteFaq(int faqNo);

    /**
     * 검색어로 FAQ 조회
     */
    @Select("""
        SELECT 
            FAQ_NO as faqNo,
            FAQ_CATEGORY as faqCategory,
            FAQ_QUESTION as faqQuestion,
            FAQ_ANSWER as faqAnswer,
            CREATED_BY as createdBy,
            CREATED_AT as createdAt,
            UPDATED_AT as updatedAt
        FROM FAQ_INFO
        WHERE USE_YN = 'Y'
          AND (FAQ_QUESTION LIKE CONCAT('%', #{keyword}, '%')
           OR FAQ_ANSWER LIKE CONCAT('%', #{keyword}, '%'))
        ORDER BY FAQ_ORDER, CREATED_AT DESC
        """)
    List<Faq> searchFaq(@Param("keyword") String keyword);
}
