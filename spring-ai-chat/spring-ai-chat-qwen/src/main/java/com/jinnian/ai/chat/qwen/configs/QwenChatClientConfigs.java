package com.jinnian.ai.chat.qwen.configs;

import com.jinnian.ai.chat.qwen.advisors.SimpleMetricAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName QwenChatClientConfigs
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/18 20:36
 */
@Configuration
public class QwenChatClientConfigs {

    @Bean
    public ChatClient qwenChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个乐于助人的AI助手，请回答用户的问题。")
                .build();
    }
}
