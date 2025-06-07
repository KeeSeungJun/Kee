package com.seonier.service;

import com.seonier.config.OpenAiPromptConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final OpenAiPromptConfiguration promptConfig;
    private final OpenAiClient openAiClient; // OpenAI 호출용 래퍼
    private final UserService userService;

    public String generatePromptForUser(String userId) {
        User user = userService.getUserByUserId(userId);

        String prompt = promptConfig.getPromptTemplate();

        prompt = prompt.replace("${birthdate}", user.getBirthdate())
                .replace("${occupation}", user.getOccupation())
                .replace("${postcode}", user.getPostcode())
                .replace("${address}", user.getUserAddr())
                .replace("${gender}", user.getGender())
                .replace("${disease}", user.getUserHealth())
                .replace("${customDisease}", user.getCustomDisease());

        return prompt;
    }

    public String getRecommendation(String userId) {
        String prompt = generatePromptForUser(userId);
        return openAiClient.ask(prompt); // 실제 OpenAI API 요청
    }
}
