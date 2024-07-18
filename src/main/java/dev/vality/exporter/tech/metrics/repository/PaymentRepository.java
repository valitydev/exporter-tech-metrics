package dev.vality.exporter.tech.metrics.repository;

import dev.vality.exporter.tech.metrics.entity.PaymentEntity;
import dev.vality.exporter.tech.metrics.entity.PaymentPk;
import dev.vality.exporter.tech.metrics.entity.PaymentsAggregatedMetricDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("LineLength")
public interface PaymentRepository extends JpaRepository<PaymentEntity, PaymentPk> {

    @Query(name = "getPaymentsStatusMetrics", nativeQuery = true)
    List<PaymentsAggregatedMetricDto> getPaymentsStatusMetrics(@Param("invoiceIds") List<String> invoiceIds);
}
