package com.jinnian.ai.chat.multi.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MultiChatContoller
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/14 22:44
 */
@RestController
@RequestMapping("/api/multi-chat")
public class MultiChatController {

    @Autowired
    @Qualifier("openAiChatClient")
    private ChatClient openAiChatClient;

    @Autowired
    @Qualifier("deepSeekChatClient")
    private ChatClient deepSeekChatClient;

    @RequestMapping("/chat")
    public String chat(){
        String dsAnswer = this.deepSeekChatClient.prompt()
                .user("你是谁?")
                .call()
                .content();

        String openAiAnswer = this.openAiChatClient.prompt()
                .user("你是谁?")
                .call()
                .content();

        return "DeepSeek: " + dsAnswer + "\nOpenAi: " + openAiAnswer;
    }
}
