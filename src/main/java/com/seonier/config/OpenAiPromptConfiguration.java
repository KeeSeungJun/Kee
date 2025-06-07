package com.seonier.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Getter
@Setter
public class OpenAiPromptConfiguration {
    private String promptTemplate;
}
