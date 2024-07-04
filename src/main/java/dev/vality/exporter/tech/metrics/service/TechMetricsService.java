package dev.vality.exporter.tech.metrics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("LineLength")
public class TechMetricsService {
    private final MeterRegistryService meterRegistryService;
    private final OpenSearchService openSearchService;
    private final ObjectMapper objectMapper;
    private final Map<String, Double> paymentApiHttpCodeCountMap;
    private final Map<String, Double> paymentMachinesFailedCountMap;
    private final Map<String, Double> payoutApiHttpCodeCountMap;
    private final Map<String, Double> payoutMachinesFailedCountMap;

    public void registerMetrics() {

    }
}
