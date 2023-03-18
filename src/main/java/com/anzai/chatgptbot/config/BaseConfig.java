package com.anzai.chatgptbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 基本配置，从配置文件读取
 *
 * @author tinybye
 * @date 2023/3/17
 */
@Configuration
@Getter
public class BaseConfig {
    @Value("${chatGPT.apiKey}")
    private String apiKey;

    @Value("${chatGPT.maxTokens}")
    private int maxTokens;

    @Value("${chatGPT.temperature}")
    private double temperature;

    @Value("${chatGPT.maxChatNum}")
    private Integer maxChatNum;

    @Value("${chatGPT.isContinuous}")
    private boolean isContinuous;
}
