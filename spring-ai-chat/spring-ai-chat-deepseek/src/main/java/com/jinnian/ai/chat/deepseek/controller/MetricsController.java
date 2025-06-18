package com.jinnian.ai.chat.deepseek.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private MeterRegistry meterRegistry;

    // DeepSeek 官方定价（2024年价格，单位：元/1M tokens）
    private static final BigDecimal PROMPT_TOKEN_PRICE = new BigDecimal("1.33");  // 输入token价格
    private static final BigDecimal COMPLETION_TOKEN_PRICE = new BigDecimal("2.66");  // 输出token价格

    @GetMapping("/ai-tokens")
    public Map<String, Double> getAiTokenMetrics() {
        Map<String, Double> metrics = new HashMap<>();
        
        Counter promptTokensCounter = meterRegistry.find("ai.prompt.tokens").counter();
        Counter completionTokensCounter = meterRegistry.find("ai.completion.tokens").counter();
        Counter totalTokensCounter = meterRegistry.find("ai.total.tokens").counter();
        
        metrics.put("promptTokens", promptTokensCounter != null ? promptTokensCounter.count() : 0.0);
        metrics.put("completionTokens", completionTokensCounter != null ? completionTokensCounter.count() : 0.0);
        metrics.put("totalTokens", totalTokensCounter != null ? totalTokensCounter.count() : 0.0);
        
        return metrics;
    }

    /**
     * 计算DeepSeek token使用费用
     * @return 费用统计信息
     */
    @GetMapping("/ai-cost")
    public Map<String, Object> getAiTokenCost() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取token统计
        Counter promptTokensCounter = meterRegistry.find("ai.prompt.tokens").counter();
        Counter completionTokensCounter = meterRegistry.find("ai.completion.tokens").counter();
        Counter totalTokensCounter = meterRegistry.find("ai.total.tokens").counter();
        
        double promptTokens = promptTokensCounter != null ? promptTokensCounter.count() : 0.0;
        double completionTokens = completionTokensCounter != null ? completionTokensCounter.count() : 0.0;
        double totalTokens = totalTokensCounter != null ? totalTokensCounter.count() : 0.0;
        
        // 计算费用（单位：元）
        BigDecimal promptCost = PROMPT_TOKEN_PRICE.multiply(new BigDecimal(promptTokens))
                .divide(new BigDecimal("1000000"), 6, RoundingMode.HALF_UP);
        BigDecimal completionCost = COMPLETION_TOKEN_PRICE.multiply(new BigDecimal(completionTokens))
                .divide(new BigDecimal("1000000"), 6, RoundingMode.HALF_UP);
        BigDecimal totalCost = promptCost.add(completionCost);
        
        // 组装返回结果
        Map<String, Object> tokenStats = new HashMap<>();
        tokenStats.put("promptTokens", (long)promptTokens);
        tokenStats.put("completionTokens", (long)completionTokens);
        tokenStats.put("totalTokens", (long)totalTokens);
        
        Map<String, Object> costStats = new HashMap<>();
        costStats.put("promptCost", promptCost.doubleValue());
        costStats.put("completionCost", completionCost.doubleValue());
        costStats.put("totalCost", totalCost.doubleValue());
        costStats.put("currency", "CNY");
        
        Map<String, Object> pricing = new HashMap<>();
        pricing.put("promptTokenPrice", PROMPT_TOKEN_PRICE.doubleValue() + " CNY/1M tokens");
        pricing.put("completionTokenPrice", COMPLETION_TOKEN_PRICE.doubleValue() + " CNY/1M tokens");
        
        result.put("tokenUsage", tokenStats);
        result.put("cost", costStats);
        result.put("pricing", pricing);
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    /**
     * 重置token统计和费用计算
     * @return 操作结果
     */
    @GetMapping("/ai-cost/reset")
    public Map<String, Object> resetAiTokenCost() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 注意：Micrometer的Counter是累加的，无法直接重置
            // 这里只是返回当前状态，实际重置需要重启应用或使用其他方式
            Counter promptTokensCounter = meterRegistry.find("ai.prompt.tokens").counter();
            Counter completionTokensCounter = meterRegistry.find("ai.completion.tokens").counter();
            Counter totalTokensCounter = meterRegistry.find("ai.total.tokens").counter();
            
            double promptTokens = promptTokensCounter != null ? promptTokensCounter.count() : 0.0;
            double completionTokens = completionTokensCounter != null ? completionTokensCounter.count() : 0.0;
            double totalTokens = totalTokensCounter != null ? totalTokensCounter.count() : 0.0;
            
            result.put("message", "注意：Micrometer Counter无法直接重置，需要重启应用来清零统计");
            result.put("currentPromptTokens", (long)promptTokens);
            result.put("currentCompletionTokens", (long)completionTokens);
            result.put("currentTotalTokens", (long)totalTokens);
            result.put("success", false);
        } catch (Exception e) {
            result.put("message", "重置失败: " + e.getMessage());
            result.put("success", false);
        }
        
        return result;
    }
}