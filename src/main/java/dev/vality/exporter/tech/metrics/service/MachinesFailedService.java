package dev.vality.exporter.tech.metrics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MachinesFailedService {

    private final OpenSearchService openSearchService;

    public void registerMetrics() {
        var machinesFailedData = openSearchService.getMachinesFailedData();
        log.info("machinesFailedData size {}", machinesFailedData.size());
    }
}
