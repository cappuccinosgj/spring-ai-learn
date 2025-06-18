package com.jinnian.ai.chat.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName OllamaChatController
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/14 23:04
 */
@RestController
@RequestMapping("/api/ollama")
public class OllamaChatController {

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "userInput", required = false, defaultValue = "你是谁？") String userInput) {
        return chatClient.prompt().user(userInput).call().content();
    }
}
