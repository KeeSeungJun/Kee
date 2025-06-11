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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.model.User;
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

    @Value("${prompt}")
    private String prompt;

    public DefaultResponse getPrompt(String userId) {
        User user = userService.findByUserId(userId);
        if (user == null || StringUtils.isEmpty(user.getUserId())) {
            throw new RequestException(401, "로그인 후 다시 이용해주세요.");
        }

        // TODO 샘플로 사용자 리스트 조회, 일자리 정보를 이런식으로 가져온다.
        List<User> users = userService.findAll();
        log.debug("User list: {}", JsonUtils.toJsonLogIndent(users));

        // DB에서 조회된사용한 정보를 이용해서 프롬프트를 완성한다.
        String content = StringUtils.replace(this.prompt, "{{USER_ID}}", user.getUserId());
//		content = StringUtils.replace(content, "{{JOBS}}", JsonUtils.toJsonLogIndent(users));
        log.debug("Prompt content: {}", content);

        OpenAiChatOptions.Builder builder = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O_MINI) // TODO OpenAI Model 설정
                .temperature(0.0) // 무작위성" 또는 "창의성"을 조절하는 파라미터
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.TEXT).build()); // 응답을 어떻게 받을지 정의

//		 TODO 실제 OpenAI 호출하는 부분
		 ChatResponse chatResponse = this.openAiChatModel.call(
		 		new Prompt(UserMessage.builder().text(content).build(), builder.build())
		 );
		 String result = chatResponse.getResult().getOutput().getText();
		 log.debug("Open AI Text: {}", result);
//
//		 응답받은 값을 정의한다.
//		 { name: "청소 업무", salary: "220만원", location: "대전 서구", company: "클린업", contact: "042-123-4567" },
//		 { name: "배달 보조", salary: "200만원", location: "대전 중구", company: "퀵익스프레스", contact: "010-2222-3333" },
//		 { name: "식당 서빙", salary: "230만원", location: "대전 유성구", company: "맛집식당", contact: "042-987-6543" },

        // TODO 이곳에 실제 데이터가 들어가야한다. 이 부분은 임의적으로 만든 것임.
        List<Map<String, String>> list = List.of(
                Map.of("name", "청소 업무", "salary", "220만원", "location", "대전 서구", "company", "클린업", "contact", "042-123-4567"),
                Map.of("name", "배달 보조", "salary", "200만원", "location", "대전 중구", "company", "퀵익스프레스", "contact", "010-2222-3333"),
                Map.of("name", "식당 서빙", "salary", "230만원", "location", "대전 유성구", "company", "맛집식당", "contact", "042-987-6543")
        );

        return DefaultResponse.builder()
                .put("list", list)
                .build();
    }
}