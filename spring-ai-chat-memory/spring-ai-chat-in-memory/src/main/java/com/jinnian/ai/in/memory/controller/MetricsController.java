package com.jinnian.ai.in.memory.controller;

import com.jinnian.ai.in.memory.service.TokenCostService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MetricsController
 * @Description 内存聊天Metrics控制器
 * @Author cappuccinosgj
 * @Date 2025/6/18
 */
@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private MeterRegistry meterRegistry;
    
    @Autowired
    private TokenCostService tokenCostService;

    /**
     * 获取AI Token使用统计
     * @return Token统计信息
     */
    @GetMapping("/ai-tokens")
    public Map<String, Double> getAiTokenMetrics() {
        Map<String, Double> metrics = new HashMap<>();
        
        Counter promptTokensCounter = meterRegistry.find("ai.memory.prompt.tokens").counter();
        Counter completionTokensCounter = meterRegistry.find("ai.memory.completion.tokens").counter();
        Counter totalTokensCounter = meterRegistry.find("ai.memory.total.tokens").counter();
        
        metrics.put("promptTokens", promptTokensCounter != null ? promptTokensCounter.count() : 0.0);
        metrics.put("completionTokens", completionTokensCounter != null ? completionTokensCounter.count() : 0.0);
        metrics.put("totalTokens", totalTokensCounter != null ? totalTokensCounter.count() : 0.0);
        
        return metrics;
    }

    /**
     * 获取AI Token费用统计
     * @return 费用统计信息
     */
    @GetMapping("/ai-cost")
    public Map<String, Object> getAiTokenCost() {
        return tokenCostService.getCostReport();
    }
}