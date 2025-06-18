package com.jinnian.ai.chat.qwen.configs;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


/**
 * @ClassName MetricConfigs
 * @Description Micrometer Metrics配置
 * @Author cappuccinosgj
 * @Date 2025/6/18
 */
@Configuration
public class MetricConfigs {

    @Autowired
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        // 将MeterRegistry注册到Metrics全局注册表
        Metrics.addRegistry(meterRegistry);
    }
}