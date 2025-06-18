package com.jinnian.ai.chat.multi.openai.service;

import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MultiChatClientService
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/13 23:06
 */
@Service
public class MultiChatClientService {

    private static final Logger logger = LoggerFactory.getLogger(MultiChatClientService.class);

    @Autowired
    private OpenAiChatModel baseChatModel;

    @Autowired
    private OpenAiApi baseOpenAiApi;

    public void multiClientFlow() {
        try {
            OpenAiApi deepseekApi = baseOpenAiApi.mutate()
                    .baseUrl("https://api.deepseek.com")
                    .completionsPath("/v1/chat/completions")
                    .apiKey(System.getenv("DEEPSEEK_API_KEY"))
                    .build();

            OpenAiApi doubaoApi = baseOpenAiApi.mutate()
                    .baseUrl("https://ark.cn-beijing.volces.com/api/v3")
                    .completionsPath("/chat/completions")
                    .apiKey(System.getenv("DOUBAO_API_KEY"))
                    .build();

            OpenAiChatModel deepseekModel = baseChatModel.mutate()
                    .openAiApi(deepseekApi)
                    .defaultOptions(OpenAiChatOptions.builder().model("deepseek-chat").temperature(0.5).build())
                    .build();

            OpenAiChatModel doubaoModel = baseChatModel.mutate()
                    .openAiApi(doubaoApi)
                    .defaultOptions(OpenAiChatOptions.builder()
                            .model("doubao-seed-1-6-250615")
                            .temperature(0.7)
                            .build())
                    .build();

            String prompt = "What is the capital of France?";
            String dsResponse = ChatClient.builder(deepseekModel).build().prompt(prompt).call().content();
            String dbResponse = ChatClient.builder(doubaoModel).build().prompt(prompt).call().content();

            logger.info("deepseek response: {}", dsResponse);
            logger.info("doubao response: {}", dbResponse);
        } catch (Exception e) {
            logger.error("Error in multi-client flow", e);
        }
    }
}
