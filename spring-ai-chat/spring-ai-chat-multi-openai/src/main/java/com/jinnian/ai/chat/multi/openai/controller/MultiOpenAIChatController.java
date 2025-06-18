package com.jinnian.ai.chat.multi.openai.controller;

import com.jinnian.ai.chat.multi.openai.service.MultiChatClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MultiOpenAIChatContoller
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/13 23:05
 */
@RestController
@RequestMapping("/api/multi-chat-openai")
public class MultiOpenAIChatController {
    @Autowired
    private MultiChatClientService multiChatClientService;

    @RequestMapping("/chat")
    public void chat() {
        this.multiChatClientService.multiClientFlow();
    }
}
