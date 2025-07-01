package com.seonier.web.rest.controller;

//import com.seonier.service.OpenAiService;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.service.OpenAiService;
import com.seonier.service.UserService;
import com.seonier.service.JobService;
import com.seonier.web.lang.AbstractController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GptController extends AbstractController {

    private final UserService userService;
    private final JobService jobService;
    private final OpenAiService openaiService;


    @GetMapping("/api/evaluate-all2")
    public DefaultResponse evaluateAll2(HttpServletRequest request) throws IOException {
        String userId = getUserIdFromCookies(request);

        DefaultResponse promptResponse = openaiService.getPrompt(userId);
        log.debug("OpenAI getPrompt() 반환값: {}", promptResponse);

        return promptResponse;
    }

}
