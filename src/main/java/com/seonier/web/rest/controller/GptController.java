package com.seonier.web.rest.controller;

//import com.seonier.service.OpenAiService;
import com.seonier.service.UserService;
import com.seonier.service.JobService;
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

@Slf4j
@RequiredArgsConstructor
@RestController
public class GptController {

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
    public String evaluateAll2(HttpServletRequest request) {
        String userId = null;
        for (Cookie cookie : request.getCookies()) {
            if ("USER_ID".equals(cookie.getName())) {
                log.debug("user_id: {}", cookie.getValue());
                userId = cookie.getValue();
                break;
            }
        }
        openaiService.getPrompt(userId);
        return "TEST";
    }

}
