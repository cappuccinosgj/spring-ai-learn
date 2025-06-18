package com.jinnian.ai.chat.doubao.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DouBaoChatController
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/13 22:50
 */
@RestController
@RequestMapping("/api/doubao")
public class DouBaoChatController {

    @Autowired
    private ChatClient chatClient;

    /**
     * 用户输入，返回聊天结果
     *
     * @param userInput 用户输入
     * @return 返回内容
     */
    @GetMapping("/chat")
    public String prompt(String userInput) {
        return chatClient.prompt(userInput).call().content();
    }

}
