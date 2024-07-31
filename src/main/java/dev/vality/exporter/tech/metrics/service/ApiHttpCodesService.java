package dev.vality.exporter.tech.metrics.service;

import dev.vality.exporter.tech.metrics.model.Metric;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiHttpCodesService {

    private final MeterRegistryService meterRegistryService;
    private final OpenSearchService openSearchService;
    private final Map<String, Double> apiHttpCodeCountMap;
    private static final String CAPI = "capi";
    private static final String WAPI = "wapi";

    public void registerMetrics() {
        var capiHttpCodeData = openSearchService.getCapiHttpCodeData();
        log.debug("capiHttpCodeData size {}", capiHttpCodeData.size());

        var wapiHttpCodeData = openSearchService.getWapiHttpCodeData();
        log.debug("wapiHttpCodeData size {}", wapiHttpCodeData.size());

        for (var capiHttpCodeDto : capiHttpCodeData) {
            meterRegistryService.gauge(apiHttpCodeCountMap, Metric.API_HTTP_CODE_COUNT,
                    getMetricId(CAPI, capiHttpCodeDto.getStatus(), capiHttpCodeDto.getHttpHost()),
                    getTags(CAPI, capiHttpCodeDto.getStatus(), capiHttpCodeDto.getHttpHost()));
        }

        for (var wapiHttpCodeDto : wapiHttpCodeData) {
            meterRegistryService.gauge(apiHttpCodeCountMap, Metric.API_HTTP_CODE_COUNT,
                    getMetricId(WAPI, wapiHttpCodeDto.getStatus(), wapiHttpCodeDto.getHttpHost()),
                    getTags(WAPI, wapiHttpCodeDto.getStatus(), wapiHttpCodeDto.getHttpHost()));
        }

        if (log.isDebugEnabled()) {
            var registeredMetricsSize =
                    meterRegistryService.getRegisteredMetricsSize(Metric.API_HTTP_CODE_COUNT.getName());
            log.debug("Limits metrics have been registered to 'prometheus', " +
                            "registeredMetricsSize = {}, capiHttpCodeData = {}, wapiHttpCodeData = {}",
                    registeredMetricsSize, capiHttpCodeData.size(), wapiHttpCodeData.size());
        }
    }

    private Tags getTags(String apiType, String code, String url) {
        return Tags.of(
                Tag.of("api_type", apiType),
                Tag.of("code", code),
                Tag.of("url", url)
        );
    }

    private String getMetricId(String apiType, String code, String url) {
        return String.format("%s:%s:%s",
                apiType,
                code,
                url);
    }
}
