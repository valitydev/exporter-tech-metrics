package dev.vality.exporter.tech.metrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachinesFailedData {
    private String id;
    private String index;
    private String scope;
    private String type;
    private String timestamp;
    private String deadline;
    private String errorClass;
    private String errorReason;
    private String errorStackTrace;
    private String kubernetesContainerHash;
    private String kubernetesContainerImage;
    private String kubernetesContainerName;
    private String kubernetesHost;
    private String kubernetesNamespaceName;
    private String mgPulseEventId;
    private String otelSpanId;
    private String otelTraceFlags;
    private String parentId;
    private String pid;
    private String spanId;
    private String stream;
}
