package com.jinnian.ai.chat.qwen.advisors;

import io.micrometer.core.instrument.Metrics;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;

/**
 * @ClassName SimpleMetricAdvisor
 * @Description 通义千问Token统计Advisor
 * @Author cappuccinosgj
 * @Date 2025/6/18
 */
public class SimpleMetricAdvisor implements CallAdvisor {

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        // 执行实际的调用
        ChatClientResponse response = chain.nextCall(request);
        // 获取响应的使用情况
        Usage usage = response.chatResponse().getMetadata().getUsage();
        // 输入 Prompt 所使用的 token 数
        int promptTokens = usage.getPromptTokens();
        // 输出 Completion 所使用的 token 数
        int completionTokens = usage.getCompletionTokens();
        // totalTokens：总 token 数（前两者之和）
        int totalTokens = usage.getTotalTokens();
        Metrics.counter("ai.qwen.prompt.tokens").increment(promptTokens);
        Metrics.counter("ai.qwen.completion.tokens").increment(completionTokens);
        Metrics.counter("ai.qwen.total.tokens").increment(totalTokens);
        return response;
    }

    @Override
    public String getName() {
        return "qwen-metric-advisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}