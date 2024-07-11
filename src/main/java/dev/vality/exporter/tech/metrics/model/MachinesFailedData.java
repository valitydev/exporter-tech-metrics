package dev.vality.exporter.tech.metrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachinesFailedData {

    @JsonProperty("machine_ns")
    private String machineNs;
    @JsonProperty("machine_id")
    private String machineId;

}
