package dev.vality.exporter.tech.metrics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiHttpCodesService {

    private final OpenSearchService openSearchService;

    public void registerMetrics() {
        var capiHttpCodeData = openSearchService.getCapiHttpCodeData();
        log.info("capiHttpCodeData {}", capiHttpCodeData);
        var wapiHttpCodeData = openSearchService.getWapiHttpCodeData();
        log.info("wapiHttpCodeData {}", wapiHttpCodeData);
    }
}
