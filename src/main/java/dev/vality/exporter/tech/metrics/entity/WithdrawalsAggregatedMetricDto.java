package dev.vality.exporter.tech.metrics.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedNativeQuery(
        name = "getWithdrawalsMetrics",
        query = """
                   with w1 as (
                       select
                         w.withdrawal_id as withdrawalId, 
                         coalesce(w.provider_id, -1) as providerId,
                         w.currency_code as currencyCode 
                       from
                         dw.withdrawal as w 
                       where w.withdrawal_id in :withdrawalIds 
                       and provider_id not in (1)
                       and w.current
                   )
                   select 
                       w1.withdrawalId, 
                       w1.providerId, 
                       p.name as providerName, 
                       w1.currencyCode 
                   from
                       w1
                       inner join dw.provider as p on w1.providerId = p.provider_ref_id
                       and p.current;
                """,
        resultSetMapping = "WithdrawalsAggregatedMetricDtoList")
@SqlResultSetMapping(
        name = "WithdrawalsAggregatedMetricDtoList",
        classes = @ConstructorResult(
                targetClass = WithdrawalsAggregatedMetricDto.class,
                columns = {
                        @ColumnResult(name = "withdrawalId", type = String.class),
                        @ColumnResult(name = "providerId", type = String.class),
                        @ColumnResult(name = "providerName", type = String.class),
                        @ColumnResult(name = "currencyCode", type = String.class),}))
@SuppressWarnings("LineLength")
public class WithdrawalsAggregatedMetricDto {

    @Id
    private Long id;
    private String withdrawalId;
    private String providerId;
    private String providerName;
    private String currencyCode;

    public WithdrawalsAggregatedMetricDto(String withdrawalId, String providerId, String providerName, String currencyCode) {
        this.withdrawalId = withdrawalId;
        this.providerId = providerId;
        this.providerName = providerName;
        this.currencyCode = currencyCode;
    }

    public WithdrawalsAggregatedMetricDto() {

    }
}
