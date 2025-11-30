package com.seonier.util;

import com.seonier.persistence.model.User;
import com.seonier.persistence.model.Job;

public class PromptBuilder {
    public static String build(User u, Job j) {
        // 건강 정보 문자열 생성
        String healthInfo = (u.getUserHealth() != null && !u.getUserHealth().isEmpty()) 
            ? u.getUserHealth() 
            : "없음";
        
        // 사용자 주소 좌표 (없으면 0.0)
        double userLat = (u.getUserAddrLat() != null) ? u.getUserAddrLat() : 0.0;
        double userLon = (u.getUserAddrLon() != null) ? u.getUserAddrLon() : 0.0;
        
        return String.format(
                "[사용자 정보]\n" +
                        "- 희망 직업: %s\n" +
                        "- 성별: %s\n" +
                        "- 주소 위도/경도: %.7f, %.7f\n" +
                        "- 건강 정보: %s\n" +
                        "- 기타 질병사항: %s\n\n" +
                        "[일자리 정보]\n" +
                        "- 제목: %s\n" +
                        "- 기업명: %s\n" +
                        "- 모집 직종: %s\n" +
                        "- 근무 예정지: %s\n" +
                        "- 급여: %s\n" +
                        "- 근무 시간: %s\n" +
                        "- 고용형태: %s\n" +
                        "- 경력: %s\n" +
                        "- 학력: %s\n" +
                        "- 우대 조건: %s\n" +
                        "- 근무 형태: %s\n" +
                        "- 인근 전철역: %s\n\n" +
                        "위 정보를 바탕으로 0에서 100점 사이의 정수로 해당 일자리와 사용자 간 적합도를 평가해 주세요. "
                        + "결과는 JSON {\"score\":<정수>,\"reason\":\"<간단한 설명>\"} 형태로만 반환해 주세요.",
                u.getOccupation() != null ? u.getOccupation() : "없음",
                u.getGender() != null ? u.getGender() : "없음",
                userLat, userLon,
                healthInfo,
                u.getCustomDisease() != null ? u.getCustomDisease() : "없음",
                j.getJobTitle() != null ? j.getJobTitle() : "",
                j.getJobCompanyName() != null ? j.getJobCompanyName() : "",
                j.getJobPosition() != null ? j.getJobPosition() : "",
                j.getJobWorkLocation() != null ? j.getJobWorkLocation() : "",
                j.getJobSalary() != null ? j.getJobSalary() : "",
                j.getJobWorkHours() != null ? j.getJobWorkHours() : "",
                j.getJobEmploymentType() != null ? j.getJobEmploymentType() : "",
                j.getJobExperience() != null ? j.getJobExperience() : "",
                j.getJobEducation() != null ? j.getJobEducation() : "",
                j.getJobPreference() != null ? j.getJobPreference() : "",
                j.getJobWorkType() != null ? j.getJobWorkType() : "",
                j.getJobNearbyStation() != null ? j.getJobNearbyStation() : ""
        );
    }
}
