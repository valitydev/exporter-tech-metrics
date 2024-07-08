package dev.vality.exporter.tech.metrics.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpCodeDto {
    private String apiType;
    private String code;
    private String url;
}
