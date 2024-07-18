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
public class PaymentPk implements Serializable {

    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "sequence_id")
    private Long sequenceId;

    @Column(name = "change_id")
    private Integer changeId;

}
