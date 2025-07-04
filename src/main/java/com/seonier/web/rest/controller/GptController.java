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

    /**
     * 로그인한 사용자와 모든 일자리를 가져와서
     * 각각 점수화 한 뒤 Map<jobNo, JSON> 형태로 반환
     */
//    @GetMapping("/api/evaluate-all")
//    public Map<Long, String> evaluateAll(Principal principal) {
//        User user = userService.getUserByUserId(principal.getName());
//        List<Job> jobs = jobService.findAll();
//
//        ChatResponse response = chatModel.call(
//                new Prompt(
//                        "Generate the names of 5 famous pirates.",
//                        OpenAiChatOptions.builder()
//                                .model("gpt-4o")
//                                .temperature(0.4)
//                                .build()
//                ));
//        log.debug(response.toString());
//        return jobs.stream()
//                .collect(Collectors.toMap(
//                        job -> job.getJobNo().longValue(),  // Integer → Long
//                        job -> {
//                            String prompt = PromptBuilder.build(user, job);
//                            return gptService.evaluateSuitability(prompt);
//                        }
//                ));
//    }

    @GetMapping("/api/evaluate-all2")
    public DefaultResponse evaluateAll2(HttpServletRequest request) throws IOException {
        String userId = getUserIdFromCookies(request);
//        for (Cookie cookie : request.getCookies()) {
//            if ("USER_ID".equals(cookie.getName())) {
//                log.debug("user_id: {}", cookie.getValue());
//                userId = cookie.getValue();
//                break;
//            }
//        }
        DefaultResponse promptResponse = openaiService.getPrompt(userId);
        log.debug("OpenAI getPrompt() 반환값: {}", promptResponse);

//        @SuppressWarnings("unchecked")
//        List<Map<String, String>> jobs = (List<Map<String, String>>) promptResponse.get("list");
//        log.debug("추출된 일자리 리스트: {}", jobs);

        return promptResponse;
    }

}
