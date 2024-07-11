package dev.vality.exporter.tech.metrics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Metric {

    API_HTTP_CODE_COUNT(
            formatWithPrefix("api_http_code_count"),
            "Counting amount of HTTP codes when accessing the processing API"),
    MACHINES_FAILED_COUNT(
            formatWithPrefix("machines_failed_count"),
            "Increase amount of fallen machines");

    @Getter
    private final String name;
    @Getter
    private final String description;

    private static String formatWithPrefix(String name) {
        return String.format("etm_%s", name);
    }
}
