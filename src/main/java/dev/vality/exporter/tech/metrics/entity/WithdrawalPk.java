package dev.vality.exporter.tech.metrics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalPk implements Serializable {

    @Column(name = "withdrawal_id")
    private String withdrawalId;

    @Column(name = "sequence_id")
    private String sequenceId;

}
