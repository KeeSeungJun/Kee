package com.seonier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 직업분류 응답 DTO
 *
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCategoryResponse {

    /**
     * 직업 코드
     */
    private String jobCode;

    /**
     * 직업명
     */
    private String jobName;

    /**
     * 대분류 코드
     */
    private String majorCode;

    /**
     * 대분류명
     */
    private String majorName;

    /**
     * 중분류 코드
     */
    private String middleCode;

    /**
     * 중분류명
     */
    private String middleName;

}
