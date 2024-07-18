package dev.vality.exporter.tech.metrics.repository;

import dev.vality.exporter.tech.metrics.entity.WithdrawalEntity;
import dev.vality.exporter.tech.metrics.entity.WithdrawalPk;
import dev.vality.exporter.tech.metrics.entity.WithdrawalsAggregatedMetricDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("LineLength")
public interface WithdrawalRepository extends JpaRepository<WithdrawalEntity, WithdrawalPk> {

    @Query(name = "getWithdrawalsMetrics", nativeQuery = true)
    List<WithdrawalsAggregatedMetricDto> getWithdrawalsMetrics(@Param("withdrawalIds") List<String> withdrawalIds);
}
