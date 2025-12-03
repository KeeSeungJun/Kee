package com.seonier.dto.request;

import lombok.Data;

/**
 * 일자리 등록 요청 DTO
 */
@Data
public class JobRegisterRequest {
    private String jobTitle;        // 업무명
    private String location;        // 근무지 주소
    private String salary;          // 급여 정보
    private String companyName;     // 업체명
    private String companyContact;  // 연락처
}
