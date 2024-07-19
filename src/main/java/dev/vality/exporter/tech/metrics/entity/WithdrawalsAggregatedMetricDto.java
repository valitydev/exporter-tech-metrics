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
                         w.withdrawal_id, 
                         coalesce(w.provider_id, -1) as provider_id,
                         w.currency_code 
                       from
                         dw.withdrawal as w 
                       where w.withdrawal_id in :withdrawalIds 
                       and provider_id not in (1)
                       and w.current
                   )
                   select 
                       w1.*, 
                       p.name as provider_name 
                   from
                       w1
                       inner join dw.provider as p on w1.provider_id = p.provider_ref_id
                       and p.current;
                """,
        resultSetMapping = "WithdrawalsAggregatedMetricDtoList")
@SqlResultSetMapping(
        name = "WithdrawalsAggregatedMetricDtoList",
        classes = @ConstructorResult(
                targetClass = WithdrawalsAggregatedMetricDto.class,
                columns = {
                        @ColumnResult(name = "providerId", type = String.class),
                        @ColumnResult(name = "providerName", type = String.class),
                        @ColumnResult(name = "currencyCode", type = String.class),
                        @ColumnResult(name = "withdrawalId", type = String.class),}))
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
