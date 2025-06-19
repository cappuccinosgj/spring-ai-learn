package com.jinnian.ai.in.memory.controller;

import com.jinnian.ai.in.memory.advisors.SimpleMetricAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


/**
 * @ClassName InMemoryChatController
 * @Description 内存聊天控制器
 * @Author cappuccinosgj
 * @Date 2025/6/18
 */
@RestController
@RequestMapping("/api/memory")
public class InMemoryChatController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    ChatMemory chatMemory;

    /**
     * 基础聊天接口（无记忆）
     * @param userInput 用户输入
     * @return 聊天响应
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String userInput) {
        return chatClient.prompt(userInput).call().content();
    }

    /**
     * 流式聊天接口（无记忆）
     * @param userInput 用户输入
     * @return 流式响应
     */
    @GetMapping(path = "/chatStream", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> chatStream(@RequestParam String userInput) {
        return chatClient.prompt(userInput).stream().content();
    }

    /**
     * 带记忆的聊天接口
     * @param userInput 用户输入
     * @param conversationId 会话ID（可选，默认为"default"）
     * @return 聊天响应
     */
    @GetMapping("/chatWithMemory")
    public String chatWithMemory(@RequestParam String userInput, 
                                @RequestParam(required = false, defaultValue = "007") String conversationId) {
        return chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(userInput)
                .call()
                .content();
    }

    /**
     * 带记忆和Token统计的聊天接口
     * @param userInput 用户输入
     * @param conversationId 会话ID（可选，默认为"default"）
     * @return 聊天响应
     */
    @GetMapping("/chatWithMemoryAndMetric")
    public String chatWithMemoryAndMetric(@RequestParam String userInput,
                                         @RequestParam(required = false, defaultValue = "default") String conversationId) {
        return chatClient.prompt()
                .advisors(a -> {
                    a.advisors(new SimpleMetricAdvisor()); // 记录Token使用情况
                    a.param(ChatMemory.CONVERSATION_ID, conversationId); // 设置会话ID
                })
                .user(userInput)
                .call()
                .content();
    }
}