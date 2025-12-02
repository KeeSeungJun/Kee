package com.seonier.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Faq {
    private Integer faqNo;
    private String faqCategory;
    private String faqQuestion;
    private String faqAnswer;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
