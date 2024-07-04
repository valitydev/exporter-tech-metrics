package dev.vality.exporter.tech.metrics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class StorageConfig {

    @Bean
    public Map<String, Double> paymentApiHttpCodeCountMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, Double> paymentMachinesFailedCountMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, Double> payoutApiHttpCodeCountMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, Double> payoutMachinesFailedCountMap() {
        return new ConcurrentHashMap<>();
    }
}
