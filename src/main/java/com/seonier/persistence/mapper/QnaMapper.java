package com.seonier.persistence.mapper;

import com.seonier.persistence.model.Qna;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QnaMapper {

    /**
     * 전체 QnA 목록 조회
     */
    @Select("""
        SELECT 
            QNA_NO as qnaNo,
            USR_ID as usrId,
            QNA_TITLE as qnaTitle,
            QNA_CONTENT as qnaContent,
            QNA_ANSWER as qnaAnswer,
            QNA_STATUS as qnaStatus,
            CREATED_AT as createdAt,
            UPDATED_AT as updatedAt,
            ANSWER_AT as answerAt,
            ANSWER_BY as answerBy
        FROM QNA_INFO
        ORDER BY 
            CASE WHEN QNA_STATUS = 'WAITING' THEN 0 ELSE 1 END,
            CREATED_AT DESC
        """)
    List<Qna> findAll();

    /**
     * QnA 번호로 조회
     */
    @Select("""
        SELECT 
            QNA_NO as qnaNo,
            USR_ID as usrId,
            QNA_TITLE as qnaTitle,
            QNA_CONTENT as qnaContent,
            QNA_ANSWER as qnaAnswer,
            QNA_STATUS as qnaStatus,
            CREATED_AT as createdAt,
            UPDATED_AT as updatedAt,
            ANSWER_AT as answerAt,
            ANSWER_BY as answerBy
        FROM QNA_INFO
        WHERE QNA_NO = #{qnaNo}
        """)
    Qna findByQnaNo(int qnaNo);

    /**
     * QnA 등록 (사용자)
     */
    @Insert("""
        INSERT INTO QNA_INFO (
            USR_ID, QNA_TITLE, QNA_CONTENT, QNA_STATUS
        ) VALUES (
            #{usrId}, #{qnaTitle}, #{qnaContent}, 'WAITING'
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "qnaNo")
    void insertQna(Qna qna);

    /**
     * 답변 등록/수정
     */
    @Update("""
        UPDATE QNA_INFO
        SET QNA_ANSWER = #{qnaAnswer},
            QNA_STATUS = 'COMPLETED',
            ANSWER_AT = CURRENT_TIMESTAMP,
            ANSWER_BY = #{answerBy}
        WHERE QNA_NO = #{qnaNo}
        """)
    void updateAnswer(Qna qna);

    /**
     * QnA 삭제
     */
    @Delete("DELETE FROM QNA_INFO WHERE QNA_NO = #{qnaNo}")
    void deleteQna(int qnaNo);

    /**
     * 검색어로 QnA 조회
     */
    @Select("""
        SELECT 
            QNA_NO as qnaNo,
            USR_ID as usrId,
            QNA_TITLE as qnaTitle,
            QNA_CONTENT as qnaContent,
            QNA_ANSWER as qnaAnswer,
            QNA_STATUS as qnaStatus,
            CREATED_AT as createdAt,
            UPDATED_AT as updatedAt,
            ANSWER_AT as answerAt,
            ANSWER_BY as answerBy
        FROM QNA_INFO
        WHERE USR_ID LIKE CONCAT('%', #{keyword}, '%')
           OR QNA_CONTENT LIKE CONCAT('%', #{keyword}, '%')
        ORDER BY 
            CASE WHEN QNA_STATUS = 'WAITING' THEN 0 ELSE 1 END,
            CREATED_AT DESC
        """)
    List<Qna> searchQna(@Param("keyword") String keyword);

    /**
     * 상태별 QnA 조회
     */
    @Select("""
        SELECT 
            QNA_NO as qnaNo,
            USR_ID as usrId,
            QNA_TITLE as qnaTitle,
            QNA_CONTENT as qnaContent,
            QNA_ANSWER as qnaAnswer,
            QNA_STATUS as qnaStatus,
            CREATED_AT as createdAt,
            UPDATED_AT as updatedAt,
            ANSWER_AT as answerAt,
            ANSWER_BY as answerBy
        FROM QNA_INFO
        WHERE QNA_STATUS = #{status}
        ORDER BY CREATED_AT DESC
        """)
    List<Qna> findByStatus(@Param("status") String status);
}
