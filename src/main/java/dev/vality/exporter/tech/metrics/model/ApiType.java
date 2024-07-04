package dev.vality.exporter.tech.metrics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApiType {
    WAPI("wapi"),
    CAPI("capi");

    @Getter
    private final String name;
}
