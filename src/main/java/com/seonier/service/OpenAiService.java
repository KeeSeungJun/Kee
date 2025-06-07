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
package com.seonier.service;

import com.seonier.persistence.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenAiService {

    private final ChatModel chatModel;
    private final UserService userService;

    @Value("${ai.prompt-template}")
    private String promptTemplate;

    /**
     * 사용자 ID를 받아 사용자 정보를 기반으로 OpenAI에 요청할 프롬프트를 생성하고 응답을 받는다.
     */
    public String getPrompt(String userId) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다: " + userId);
        }

        log.debug("사용자 정보: {}", user);

        // 사용자 정보를 템플릿에 치환
        String prompt = fillPromptTemplate(user);

        // OpenAI 요청
        ChatResponse response = chatModel.call(
                new Prompt(
                        prompt,
                        OpenAiChatOptions.builder()
                                .model(OpenAiApi.ChatModel.GPT_4_O_MINI.value)
                                .temperature(0.4)
                                .build()
                )
        );

        // 응답 텍스트 반환
        return response.getResult().getOutput().getContent();
    }

    /**
     * 사용자 정보를 프롬프트 템플릿에 치환
     */
    private String fillPromptTemplate(User user) {
        String prompt = this.promptTemplate;
        prompt = StringUtils.replace(prompt, "{{AGE}}", user.getBirthdate());  // 또는 나이로 변환
        prompt = StringUtils.replace(prompt, "{{ADDR}}", user.getUserAddr());
        prompt = StringUtils.replace(prompt, "{{GENDER}}", user.getGender());
        prompt = StringUtils.replace(prompt, "{{OCCUPATION}}", user.getOccupation());
        prompt = StringUtils.replace(prompt, "{{DISEASE}}", user.getUserHealth());
        prompt = StringUtils.replace(prompt, "{{CUSTOM_DISEASE}}", user.getCustomDisease());
        return prompt;
    }
}
