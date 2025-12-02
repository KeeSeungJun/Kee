//package com.seonier.service;
//
//import com.seonier.persistence.model.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class OpenAiService {
//
//    private final ChatModel chatModel;
//    private final UserService userService;
//
//    @Value("prompt")
//    private String prompt;
//
//
//    public void getPrompt(String userId) {
//        //DB에서 조회 후 프롬포트에 데이터 추가
//        User user = userService.findByUserId(userId);
//        log.debug(user.toString());
//
////        ChatResponse response = chatModel.call(
////                new Prompt(
////                        getReplace(age, "주소"),
////                        OpenAiChatOptions.builder()
//////                                .model("gpt-4o-mini")
////                                .model(OpenAiApi.ChatModel.GPT_4_O_MINI.value)
////                                .temperature(0.4)
////                                .build()
////                ));
//
////        return ChatResponse.builder().build();
//    }
//    private String getReplace(String age, String addr) {
//        // DB에서 정보 조회
//        String prompt =  StringUtils.replace(this.prompt, "{{AGE}}", age);
//        prompt =  StringUtils.replace(prompt, "{{ADDR}}", addr);
//        return prompt;
//    }
//}
//package com.seonier.service;
//
//import com.seonier.persistence.model.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class OpenAiService {
//
//    private final ChatModel chatModel;
//    private final UserService userService;
//
//    @Value("${ai.prompt-template}")
//    private String promptTemplate;
//
//    /**
//     * 사용자 ID를 받아 사용자 정보를 기반으로 OpenAI에 요청할 프롬프트를 생성하고 응답을 받는다.
//     */
//    public String getPrompt(String userId) {
//        User user = userService.findByUserId(userId);
//        if (user == null) {
//            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다: " + userId);
//        }
//
//        log.debug("사용자 정보: {}", user);
//
//        // 사용자 정보를 템플릿에 치환
//        String prompt = fillPromptTemplate(user);
//
//        // OpenAI 요청
//        ChatResponse response = chatModel.call(
//                new Prompt(
//                        prompt,
//                        OpenAiChatOptions.builder()
//                                .model(OpenAiApi.ChatModel.GPT_4_O_MINI.value)
//                                .temperature(0.4)
//                                .build()
//                )
//        );
//
//        // 응답 텍스트 반환
//        return response.getResult().getOutput().getText();
//    }
//
//    /**
//     * 사용자 정보를 프롬프트 템플릿에 치환
//     */
//    private String fillPromptTemplate(User user) {
//        String prompt = this.promptTemplate;
//        prompt = StringUtils.replace(prompt, "{{AGE}}", String.valueOf(user.getBirthdate()));  // 또는 나이로 변환
//        prompt = StringUtils.replace(prompt, "{{ADDR}}", user.getUserAddr());
//        prompt = StringUtils.replace(prompt, "{{GENDER}}", user.getGender());
//        prompt = StringUtils.replace(prompt, "{{OCCUPATION}}", user.getOccupation());
//        prompt = StringUtils.replace(prompt, "{{DISEASE}}", user.getUserHealth());
//        prompt = StringUtils.replace(prompt, "{{CUSTOM_DISEASE}}", user.getCustomDisease());
//        return prompt;
//    }
//}
//package com.seonier.service;
//
//import com.seonier.persistence.model.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.openai.api.OpenAiApi;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class OpenAiService {
//
//    private final ChatModel chatModel;
//    private final UserService userService;
//
//    @Value("${prompt}")
//    private String promptTemplate;
//
//    /**
//     * 사용자 ID를 받아 사용자 정보를 기반으로 OpenAI에 요청할 프롬프트를 생성하고 응답을 받는다.
//     */
//    public String getPrompt(String userId) {
//        User user = userService.findByUserId(userId);
//        if (user == null) {
//            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다: " + userId);
//        }
//
//        log.debug("사용자 정보: {}", user);
//
//        // 사용자 정보를 템플릿에 치환
//        String prompt = fillPromptTemplate(user);
//        log.info("최종 프롬프트: \n{}", prompt);
//
//        // OpenAI 요청
//        ChatResponse response = chatModel.call(
//                new Prompt(
//                        prompt,
//                        OpenAiChatOptions.builder()
//                                .model(OpenAiApi.ChatModel.GPT_4_O_MINI.value)
//                                .temperature(0.4)
//                                .build()
//                )
//        );
//
//        // GPT 응답 디버깅용 전체 출력
//        log.info("GPT 응답 전체: {}", response);
//        log.info("GPT result: {}", response.getResult());
//        log.info("GPT output text: {}", response.getResult().getOutput().getText());
//
//// 실제 응답 추출
//        String resultText = response.getResult().getOutput().getText();
//        if (StringUtils.isBlank(resultText)) {
//            resultText = "[⚠ GPT로부터 응답을 받지 못했습니다]";
//        }
//
//        return resultText;
//
//    }
//
//    /**
//     * 사용자 정보를 프롬프트 템플릿에 치환
//     */
//    private String fillPromptTemplate(User user) {
//        String prompt = this.promptTemplate;
//        prompt = StringUtils.replace(prompt, "{{AGE}}", String.valueOf(user.getBirthdate()));  // 또는 나이 계산 가능
//        prompt = StringUtils.replace(prompt, "{{ADDR}}", user.getUserAddr());
//        prompt = StringUtils.replace(prompt, "{{GENDER}}", user.getGender());
//        prompt = StringUtils.replace(prompt, "{{OCCUPATION}}", user.getOccupation());
//        prompt = StringUtils.replace(prompt, "{{DISEASE}}", user.getUserHealth());
//        prompt = StringUtils.replace(prompt, "{{CUSTOM_DISEASE}}", user.getCustomDisease());
//        return prompt;
//    }
//}
package com.seonier.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;

import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.model.User;
import com.seonier.persistence.model.Job;
import com.seonier.util.JsonUtils;
import com.seonier.web.lang.RequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenAiService {

    private final OpenAiChatModel openAiChatModel;
    private final UserService userService;
    private final JobService jobService;

    @Value("${prompt}")
    private String promptTemplate;

    public DefaultResponse getPrompt(String userId) throws IOException {
        User user = userService.findByUserId(userId);
        if (user == null || StringUtils.isEmpty(user.getUserId())) {
            throw new RequestException(401, "로그인 후 다시 이용해주세요.");
        }

        // DB에서 모든 일자리 가져오기
        List<Job> jobs = jobService.findAll();
        if (jobs == null || jobs.isEmpty()) {
            throw new RequestException(404, "추천 가능한 일자리가 없습니다.");
        }

        // 1. 위도/경도가 있는 일자리만 필터링 (지도 표시를 위해 필수)
        // ⚠️ 임시로 비활성화: 현재 DB 데이터에 좌표가 없어서 모든 데이터가 필터링됨
        // TODO: 스크래핑 시 Kakao API 좌표 변환 로직 수정 후 다시 활성화
        List<Job> jobsWithLocation = jobs.stream()
                .filter(job -> job.getJobLocationLat() != null && job.getJobLocationLon() != null)
                .toList();
        
        log.info("좌표가 있는 일자리 수: {} (전체: {})", jobsWithLocation.size(), jobs.size());
        
        // 좌표가 있는 일자리가 없으면 경고만 출력하고 전체 데이터 사용
        if (jobsWithLocation.isEmpty()) {
            log.warn("⚠️ 위치 정보가 있는 일자리가 없습니다. 전체 일자리로 추천을 진행합니다.");
            log.warn("⚠️ 지도에는 마커가 표시되지 않을 수 있습니다.");
        } else {
            jobs = jobsWithLocation;  // 좌표가 있으면 해당 데이터만 사용
        }

        // 2. 사용자의 희망 직종이 있으면 관련 일자리만 필터링
        String userHopeJobName = user.getUsrHopeJobName();
        if (userHopeJobName != null && !userHopeJobName.trim().isEmpty()) {
            log.debug("사용자 희망 직종: {}", userHopeJobName);
            // jobPosition이 희망 직종과 일치하거나 유사한 일자리만 필터링
            List<Job> filteredJobs = jobs.stream()
                    .filter(job -> job.getJobPosition() != null && 
                           (job.getJobPosition().contains(userHopeJobName) || 
                            userHopeJobName.contains(job.getJobPosition())))
                    .toList();
            
            log.info("희망 직종 필터링 결과: {} (좌표 있는 일자리: {})", filteredJobs.size(), jobs.size());
            
            if (filteredJobs.isEmpty()) {
                log.warn("희망 직종 '{}' 관련 일자리가 없어 전체 일자리로 추천합니다.", userHopeJobName);
            } else {
                jobs = filteredJobs;
            }
        } else {
            log.debug("희망 직종이 설정되지 않아 전체 일자리를 대상으로 추천합니다.");
        }

        // 일자리 목록을 프롬프트 형식으로 변환
        String jobListPrompt = buildJobListPrompt(jobs);
        
        // 프롬프트 템플릿에 일자리 목록 삽입
        String content = promptTemplate;
        content = StringUtils.replace(content, "{{JOB_LIST}}", jobListPrompt.trim());

        // 사용자 정보 치환
        content = StringUtils.replace(content, "{{AGE}}", String.valueOf(user.getBirthdate()));
        content = StringUtils.replace(content, "{{ADDR}}", user.getUserAddr() != null ? user.getUserAddr() : "정보 없음");
        content = StringUtils.replace(content, "{{GENDER}}", user.getGender() != null ? user.getGender() : "정보 없음");
        content = StringUtils.replace(content, "{{OCCUPATION}}", user.getOccupation() != null ? user.getOccupation() : "정보 없음");
        content = StringUtils.replace(content, "{{DISEASE}}", user.getUserHealth() != null ? user.getUserHealth() : "없음");
        content = StringUtils.replace(content, "{{CUSTOM_DISEASE}}", user.getCustomDisease() != null ? user.getCustomDisease() : "없음");

        log.debug("Prompt content: {}", content);

        OpenAiChatOptions.Builder builder = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O_MINI)
                .temperature(0.0)
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.TEXT).build());

		 ChatResponse chatResponse = this.openAiChatModel.call(
		 		new Prompt(UserMessage.builder().text(content).build(), builder.build())
		 );
		 String result = chatResponse.getResult().getOutput().getText();
		 log.debug("Open AI Text: {}", result);

        return DefaultResponse.builder()
                .put("list", JsonUtils.toObject(result, new TypeReference<List<Map<String, Object>>>() {}))
                .build();
    }

    /**
     * 일자리 목록을 프롬프트 형식으로 변환
     */
    private String buildJobListPrompt(List<Job> jobs) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            sb.append(String.format("  %d. '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'\n",
                i + 1,
                nvl(job.getJobPosition()),          // 1. 모집 직종 (직업 이름)
                nvl(job.getJobTitle()),              // 2. 채용공고 제목 (업무 명)
                nvl(job.getJobCompanyName()),        // 3. 기업명
                nvl(job.getJobEmploymentType()),     // 4. 고용형태 (정규직/계약직 등)
                nvl(job.getJobSalary()),             // 5. 급여 정보
                nvl(job.getJobWorkHours()),          // 6. 근무 시간
                nvl(job.getJobPreference()),         // 7. 우대 조건
                nvl(job.getJobWorkLocation()),       // 8. 근무지 주소
                formatCoordinate(job.getJobLocationLat()),  // 9. 위도
                formatCoordinate(job.getJobLocationLon()),  // 10. 경도
                nvl(job.getJobNearbyStation())       // 11. 인근 전철역
            ));
        }
        
        return sb.toString();
    }

    /**
     * null 값을 "-"로 변환
     */
    private String nvl(String value) {
        return value != null && !value.trim().isEmpty() ? value : "-";
    }
    
    /**
     * 좌표 값 포맷팅 (null이면 "-")
     */
    private String formatCoordinate(Double value) {
        return value != null ? String.format("%.6f", value) : "-";
    }
}