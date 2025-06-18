package com.jinnian.ai.chat.deepseek.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * In all cases, you need to disable the ChatClient.Builder autoconfiguration by setting the property
 * > spring.ai.chat.client.enabled=false.
 *
 * @ClassName DeepSeekChatClientConfigs
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/12 22:58
 */
@Configuration
public class DeepSeekChatClientConfigs {

    @Bean
    public ChatClient deepSeekChatClient(DeepSeekChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个非常厉害的AI助手，请回答我的问题。")
//                .defaultSystem("You are deepseek chat bot, you answer questions in a concise and accurate manner.")
                .build();
    }

}
