package dev.vality.exporter.tech.metrics.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedNativeQuery(
        name = "getPaymentsStatusMetrics",
        query = """
                with p1 as (
                    select
                      pm.invoice_id as invoiceId,
                      coalesce(pr.route_provider_id, -1) as providerId, 
                      pm.currency_code as currencyCode 
                    from
                      dw.payment as pm
                      inner join dw.payment_route as pr on pm.invoice_id = pr.invoice_id
                      and pr.route_provider_id not in (1)
                      and pr.current
                    where
                      pm.invoice_id in :invoiceIds
                )
                select
                  p1.invoiceId, 
                  p1.providerId, 
                  p.name as providerName, 
                  p1.currencyCode
                from
                  p1
                  inner join dw.provider as p on p1.providerId = p.provider_ref_id
                  and p.current
                """,
        resultSetMapping = "PaymentsAggregatedMetricDtoList")
@SqlResultSetMapping(
        name = "PaymentsAggregatedMetricDtoList",
        classes = @ConstructorResult(
                targetClass = PaymentsAggregatedMetricDto.class,
                columns = {
                        @ColumnResult(name = "invoiceId", type = String.class),
                        @ColumnResult(name = "providerId", type = String.class),
                        @ColumnResult(name = "providerName", type = String.class),
                        @ColumnResult(name = "currencyCode", type = String.class),}))
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
