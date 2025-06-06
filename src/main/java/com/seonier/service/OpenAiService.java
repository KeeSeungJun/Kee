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

    @Value("prompt")
    private String prompt;


    public void getPrompt(String userId) {
        //DB에서 조회 후 프롬포트에 데이터 추가
        User user = userService.findByUserId(userId);
        log.debug(user.toString());

//        ChatResponse response = chatModel.call(
//                new Prompt(
//                        getReplace(age, "주소"),
//                        OpenAiChatOptions.builder()
////                                .model("gpt-4o-mini")
//                                .model(OpenAiApi.ChatModel.GPT_4_O_MINI.value)
//                                .temperature(0.4)
//                                .build()
//                ));

//        return ChatResponse.builder().build();
    }
    private String getReplace(String age, String addr) {
        // DB에서 정보 조회
        String prompt =  StringUtils.replace(this.prompt, "{{AGE}}", age);
        prompt =  StringUtils.replace(prompt, "{{ADDR}}", addr);
        return prompt;
    }
}
