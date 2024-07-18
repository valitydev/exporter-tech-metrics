package dev.vality.exporter.tech.metrics.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedNativeQuery(
        name = "getPaymentsStatusMetrics",
        query = """
                select
                  pm.invoice_id as invoiceId, 
                  pr.route_provider_id as providerId,
                  p.name as providerName,
                  pm.currency_code as currencyCode
                from
                  dw.payment as pm
                  inner join dw.payment_route as pr on pm.invoice_id = pr.invoice_id
                  inner join dw.provider as p on pr.route_provider_id = p.provider_ref_id
                where
                  pm.invoice_id = in :invoiceIds
                """,
        resultSetMapping = "PaymentsAggregatedMetricDtoList")
@SqlResultSetMapping(
        name = "PaymentsAggregatedMetricDtoList",
        classes = @ConstructorResult(
                targetClass = PaymentsAggregatedMetricDto.class,
                columns = {
                        @ColumnResult(name = "providerId", type = String.class),
                        @ColumnResult(name = "providerName", type = String.class),
                        @ColumnResult(name = "currencyCode", type = String.class),
                        @ColumnResult(name = "invoiceId", type = String.class),}))
@SuppressWarnings("LineLength")
public class PaymentsAggregatedMetricDto {

    @Id
    private Long id;
    private String invoiceId;
    private String providerId;
    private String providerName;
    private String currencyCode;

    public PaymentsAggregatedMetricDto(String invoiceId, String providerId, String providerName, String currencyCode) {
        this.invoiceId = invoiceId;
        this.providerId = providerId;
        this.providerName = providerName;
        this.currencyCode = currencyCode;
    }

    public PaymentsAggregatedMetricDto() {

    }
}
