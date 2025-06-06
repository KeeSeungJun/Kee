package com.seonier.util;

import com.seonier.persistence.model.User;
import com.seonier.persistence.model.Job;

public class PromptBuilder {
    public static String build(User u, Job j) {
        return String.format(
                "[사용자 정보]\n" +
                        "- 기존 직업: %s\n" +
                        "- 성별: %s\n" +
                        "- 주소 위도/경도: %.7f, %.7f\n" +
                        "- 질병: %s, %s, %s, %s, %s, %s, %s, %s\n" +
                        "- 기타 질병사항: %s\n\n" +
                        "[일자리 정보]\n" +
                        "- 제목: %s\n" +
                        "- 업무: %s\n" +
                        "- 상세 설명: %s\n" +
                        "- 필수 자격: %s\n" +
                        "- 계약 기간: %s\n" +
                        "- 급여: %s\n" +
                        "- 근무 시간: %s\n" +
                        "- 복지 조건: %s\n" +
                        "- 근무지 위도/경도: %.7f, %.7f\n\n" +
                        "위 정보를 바탕으로 0에서 100점 사이의 정수로 해당 일자리와 사용자 간 적합도를 평가해 주세요. "
                        + "결과는 JSON {\"score\":<정수>,\"reason\":\"<간단한 설명>\"} 형태로만 반환해 주세요.",
                u.getOccupation(),
                u.getGender(),
                u.getUserAddrLat(), u.getUserAddrLon(),
//                u.getUserHealth1(), u.getUserHealth2(), u.getUserHealth3(), u.getUserHealth4(),
//                u.getUserHealth5(), u.getUserHealth6(), u.getUserHealth7(), u.getUserHealth8(),
                u.getOccupation(),
                j.getJobTitle(),
                j.getJobTask(),
                j.getJobDesc(),
                j.getJobLicenseRequired(),
                j.getJobContractPeriod(),
                j.getJobSalary(),
                j.getJobWorkHours(),
                j.getJobBenefits(),
                j.getJobLatitude(), j.getJobLongitude()
        );
    }
}
