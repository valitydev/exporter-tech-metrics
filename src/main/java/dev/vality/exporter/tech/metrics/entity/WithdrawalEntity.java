package dev.vality.exporter.tech.metrics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "withdrawal")
public class WithdrawalEntity implements Serializable {

    @EmbeddedId
    private WithdrawalPk pk;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "current")
    private boolean current;
}
