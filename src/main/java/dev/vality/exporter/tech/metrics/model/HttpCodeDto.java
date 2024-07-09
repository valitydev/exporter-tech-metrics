package dev.vality.exporter.tech.metrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpCodeDto {
    private String id;
    private String index;
    private String scope;
    private String type;
    private String timestamp;
    private String version;
    private String bodyBytesSent;
    private String httpHost;
    private String httpReferrer;
    private String httpUserAgent;
    private String httpXForwardedFor;
    private String httpXRequestId;
    private String kubernetesContainerHash;
    private String kubernetesContainerImage;
    private String kubernetesContainerName;
    private String kubernetesHost;
    private String kubernetesNamespaceName;
    private String requestLength;
    private String requestMethod;
    private String requestTime;
    private String status;
    private String stream;
    private String tlsCipher;
    private String tlsProtocol;
    private String traceUrl;
    private String upstreamCacheStatus;
}
