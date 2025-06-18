package com.jinnian.ai.chat.deepseek.configs;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MetricConfigs
 * @Description
 * @Author cappuccinosgj
 * @Date 2025/6/12 23:01
 */
@Configuration
public class MetricConfigs {

    @Autowired
    private MeterRegistry registry;

    /**
     * 在应用启动时注册 MeterRegistry
     */
    @PostConstruct
    public void init() {
        Metrics.addRegistry(registry);
    }
}
