package com.jinnian.ai.chat.qwen.controller;

import com.jinnian.ai.chat.qwen.service.TokenCostService;
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
 * @Description 通义千问Metrics控制器
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

    @GetMapping("/ai-tokens")
    public Map<String, Double> getAiTokenMetrics() {
        Map<String, Double> metrics = new HashMap<>();
        
        Counter promptTokensCounter = meterRegistry.find("ai.qwen.prompt.tokens").counter();
        Counter completionTokensCounter = meterRegistry.find("ai.qwen.completion.tokens").counter();
        Counter totalTokensCounter = meterRegistry.find("ai.qwen.total.tokens").counter();
        
        metrics.put("promptTokens", promptTokensCounter != null ? promptTokensCounter.count() : 0.0);
        metrics.put("completionTokens", completionTokensCounter != null ? completionTokensCounter.count() : 0.0);
        metrics.put("totalTokens", totalTokensCounter != null ? totalTokensCounter.count() : 0.0);
        
        return metrics;
    }

    /**
     * 获取通义千问token费用统计
     * @return 费用统计信息
     */
    @GetMapping("/ai-cost")
    public Map<String, Object> getAiTokenCost() {
        return tokenCostService.getCostReport();
    }
}