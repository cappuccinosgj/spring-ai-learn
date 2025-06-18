package com.jinnian.ai.chat.qwen.controller;

import com.jinnian.ai.chat.qwen.advisors.SimpleMetricAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @ClassName QwenChatClientController
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/18 20:38
 */
@RestController
@RequestMapping("/api/qwen")
public class QwenChatClientController {

    @Autowired
    private ChatClient chatClient;

    /**
     * 流式聊天接口（无记忆）
     * @param userInput 用户输入
     * @return 流式响应
     */
    @GetMapping(path = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> chat(@RequestParam String userInput){
        return chatClient.prompt(userInput).stream().content();
    }
    
    /**
     * 带Token统计的聊天接口
     * @param userInput 用户输入
     * @return 聊天响应
     */
    @GetMapping("/chatWithMetric")
    public String chatWithMetric(@RequestParam String userInput) {
        SimpleMetricAdvisor metricAdvisor = new SimpleMetricAdvisor();
        return chatClient.prompt()
                .advisors(metricAdvisor)
                .user(userInput)
                .call()
                .content();
    }
    
}
