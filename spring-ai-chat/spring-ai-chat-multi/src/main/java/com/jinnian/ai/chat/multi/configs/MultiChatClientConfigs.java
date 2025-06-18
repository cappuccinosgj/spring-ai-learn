package com.jinnian.ai.chat.multi.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 不同模型类型的 ChatClient 配置示例
 *
 * In all cases, you need to disable the ChatClient.Builder autoconfiguration by setting the property
 * > spring.ai.chat.client.enabled=false.
 *
 * @ClassName MultiChatClientConfigs
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/14 22:35
 */
@Configuration
@ConditionalOnProperty(name = "spring.ai.chat.client.enabled", havingValue = "false")
public class MultiChatClientConfigs {

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel){
        return ChatClient.create(chatModel);
    }

    @Bean
    public ChatClient deepSeekChatClient(DeepSeekChatModel chatModel){
        return ChatClient.builder(chatModel)
//                .defaultSystem("You are a friendly chat bot that answers question with json always")
                .build();
    }
}
