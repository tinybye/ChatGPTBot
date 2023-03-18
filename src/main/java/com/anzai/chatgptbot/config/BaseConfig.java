package com.anzai.chatgptbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 基本配置
 *
 * @author tinybye
 * @date 2023/3/17
 */
@Configuration
@Getter
public class BaseConfig {
    @Value("${chatgpt.apiKey}")
    private String chatGptApiKey;

    @Value("${chatgpt.maxTokens}")
    private int maxTokens;

    @Value("${chatgpt.temperature}")
    private double temperature;

    @Value("${chatgpt.maxChatNum}")
    private Integer maxChatNum;

    @Value("${chatgpt.isContinuous}")
    private boolean isContinuous;
}
