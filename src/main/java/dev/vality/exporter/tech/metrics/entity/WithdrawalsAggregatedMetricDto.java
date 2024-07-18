package dev.vality.exporter.tech.metrics.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedNativeQuery(
        name = "getWithdrawalsMetrics",
        query = """
                 select
                  w.withdrawal_id as withdrawalId, 
                  w.provider_id as providerId,
                  p.name as providerName,
                  w.currency_code as currencyCode
                from
                  dw.withdrawal as w
                  inner join dw.provider as p on w.provider_id = p.provider_ref_id
                where
                  w.withdrawal_id in :withdrawalIds
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
