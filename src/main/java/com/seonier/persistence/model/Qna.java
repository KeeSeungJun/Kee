package com.seonier.persistence.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Qna {
    private Integer qnaNo;           // 문의 번호
    private String usrId;            // 문의자 아이디
    private String qnaTitle;         // 문의 제목
    private String qnaContent;       // 문의 내용
    private String qnaAnswer;        // 답변 내용
    private String qnaStatus;        // 상태 (WAITING, COMPLETED)
    private LocalDateTime createdAt; // 문의 일시
    private LocalDateTime updatedAt; // 수정 일시
    private LocalDateTime answerAt;  // 답변 일시
    private String answerBy;         // 답변자 ID
}
