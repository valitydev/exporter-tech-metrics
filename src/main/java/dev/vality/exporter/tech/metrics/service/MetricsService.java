package dev.vality.exporter.tech.metrics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final ApiHttpCodesService apiHttpCodesService;
    private final MachinesFailedService machinesFailedService;

    public void registerMetrics() {
        apiHttpCodesService.registerMetrics();
        machinesFailedService.registerMetrics();
    }
}
