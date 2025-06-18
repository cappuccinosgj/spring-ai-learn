package com.jinnian.ai.chat.qwen.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TokenCostService
 * @Description 通义千问Token费用计算服务
 * @Author cappuccinosgj
 * @Date 2025/6/18
 */
@Service
public class TokenCostService {

    @Autowired
    private MeterRegistry meterRegistry;

    // 通义千问定价（参考价格，实际以阿里云官方为准）
    private static final BigDecimal PROMPT_TOKEN_PRICE = new BigDecimal("0.50");  // 输入：0.50元/1M tokens
    private static final BigDecimal COMPLETION_TOKEN_PRICE = new BigDecimal("1.50");  // 输出：1.50元/1M tokens

    /**
     * 获取当前token使用统计
     * @return token使用统计
     */
    public Map<String, Long> getTokenUsage() {
        Map<String, Long> usage = new HashMap<>();
        
        Counter promptTokensCounter = meterRegistry.find("ai.qwen.prompt.tokens").counter();
        Counter completionTokensCounter = meterRegistry.find("ai.qwen.completion.tokens").counter();
        Counter totalTokensCounter = meterRegistry.find("ai.qwen.total.tokens").counter();
        
        usage.put("promptTokens", promptTokensCounter != null ? (long)promptTokensCounter.count() : 0L);
        usage.put("completionTokens", completionTokensCounter != null ? (long)completionTokensCounter.count() : 0L);
        usage.put("totalTokens", totalTokensCounter != null ? (long)totalTokensCounter.count() : 0L);
        
        return usage;
    }

    /**
     * 计算token费用
     * @param promptTokens 输入token数量
     * @param completionTokens 输出token数量
     * @return 费用信息
     */
    public Map<String, Object> calculateCost(long promptTokens, long completionTokens) {
        Map<String, Object> costInfo = new HashMap<>();
        
        // 计算费用（单位：元）
        BigDecimal promptCost = PROMPT_TOKEN_PRICE.multiply(new BigDecimal(promptTokens))
                .divide(new BigDecimal("1000000"), 6, RoundingMode.HALF_UP);
        BigDecimal completionCost = COMPLETION_TOKEN_PRICE.multiply(new BigDecimal(completionTokens))
                .divide(new BigDecimal("1000000"), 6, RoundingMode.HALF_UP);
        BigDecimal totalCost = promptCost.add(completionCost);
        
        costInfo.put("promptCost", promptCost.doubleValue());
        costInfo.put("completionCost", completionCost.doubleValue());
        costInfo.put("totalCost", totalCost.doubleValue());
        costInfo.put("currency", "CNY");
        
        return costInfo;
    }

    /**
     * 获取完整的费用报告
     * @return 完整费用报告
     */
    public Map<String, Object> getCostReport() {
        Map<String, Object> report = new HashMap<>();
        
        // 获取token使用情况
        Map<String, Long> usage = getTokenUsage();
        
        // 计算费用
        Map<String, Object> cost = calculateCost(
                usage.get("promptTokens"), 
                usage.get("completionTokens")
        );
        
        // 定价信息
        Map<String, String> pricing = new HashMap<>();
        pricing.put("promptTokenPrice", PROMPT_TOKEN_PRICE + " CNY/1M tokens");
        pricing.put("completionTokenPrice", COMPLETION_TOKEN_PRICE + " CNY/1M tokens");
        pricing.put("note", "价格基于通义千问参考定价，实际费用以阿里云官方账单为准");
        
        report.put("tokenUsage", usage);
        report.put("cost", cost);
        report.put("pricing", pricing);
        report.put("timestamp", System.currentTimeMillis());
        
        return report;
    }
}