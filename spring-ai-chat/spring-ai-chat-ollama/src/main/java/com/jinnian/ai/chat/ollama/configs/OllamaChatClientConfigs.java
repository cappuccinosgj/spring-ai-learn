package com.jinnian.ai.chat.ollama.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName OllamaChatClientConfigs
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/14 22:59
 */
@Configuration
public class OllamaChatClientConfigs {

    /**
     * 1、这里使用的是 Ollama 协议的 ChatModel，因此这里动态注入的 ChatModel 是 OllamaChatModel
     * <p>
     * 2、这里 chatClient 设置了默认的系统提示语，会将所有的聊天请求都带上这个系统提示语，返回的内容均为 JSON 格式
     *
     * @param chatModel
     * @return ChatClient
     */
    @Bean
    public ChatClient chatClient(OllamaChatModel chatModel) {
        return ChatClient.builder(chatModel)
//                .defaultSystem("你是一个有用的助手，请回答用户的问题。")
                .build();
    }
}
