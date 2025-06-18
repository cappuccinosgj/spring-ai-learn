package com.jinnian.ai.chat.multi.openai.configs;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MultiOpenaiChatClientConfigs
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/13 23:02
 */
@Configuration
public class MultiOpenAIChatClientConfigs {
    /**
     * 这里配置一个默认的 OpenAI API 客户端；这里参考了官方文档，但是官方文档中是自动注入 baseOpenAiApi，实际上有问题
     * 因为 OpenAiApi 并没有在自动配置中注册，所以需要手动创建一个 Bean。
     *
     * @return OpenAiApi
     */
    @Bean
    public OpenAiApi baseOpenAiApi() {
        return OpenAiApi.builder()
                .apiKey(System.getenv("DEEPSEEK_API_KEY"))
                .build();
    }
}
