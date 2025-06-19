package com.jinnian.ai.in.memory.configs;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName InMemoryConfigs
 * @Description 内存聊天配置类
 * @Author cappuccinosgj
 * @Date 2025/6/18
 */
@Configuration
public class InMemoryConfigs {

    @Autowired
    private OpenAiChatModel openAiChatModel;


    /**
     * 将消息窗口维护到指定的最大大小。当消息数超过最大值时，将删除较旧的消息，同时保留系统消息。
     * 默认窗口消息个数为 20 条。
     * @return MessageWindowChatMemory实例
     */
    @Bean
    public ChatMemory messageWindowChatMemory () {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatMemory chatMemory) {
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    /**
     * 将MeterRegistry注册到Metrics全局注册表
     * @param meterRegistry MeterRegistry实例
     */
    @Autowired
    public void configureMetrics(MeterRegistry meterRegistry) {
        Metrics.addRegistry(meterRegistry);
    }
}