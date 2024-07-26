package dev.vality.exporter.tech.metrics.service;

import dev.vality.exporter.tech.metrics.entity.PaymentsAggregatedMetricDto;
import dev.vality.exporter.tech.metrics.entity.WithdrawalsAggregatedMetricDto;
import dev.vality.exporter.tech.metrics.model.MachinesFailedData;
import dev.vality.exporter.tech.metrics.model.Metric;
import dev.vality.exporter.tech.metrics.repository.PaymentRepository;
import dev.vality.exporter.tech.metrics.repository.WithdrawalRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MachinesFailedService {

    private final MeterRegistryService meterRegistryService;
    private final OpenSearchService openSearchService;
    private final PaymentRepository paymentRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final Map<String, Double> machinesFailedCountMap;
    private static final String WITHDRAWAL = "withdrawal";
    private static final String INVOICE = "invoice";


    public void registerMetrics() {
        var machinesFailedData = openSearchService.getMachinesFailedData();
        log.info("machinesFailedData {}", machinesFailedData);

        if (machinesFailedData.isEmpty()) {
            return;
        }

        var withdrawalIds = machinesFailedData.stream()
                .filter(w -> w.getMachineNs().contains(WITHDRAWAL))
                .map(MachinesFailedData::getMachineId)
                .limit(10)
                .toList();
        log.info("withdrawalIds {}", withdrawalIds);
        var withdrawalEntities = withdrawalRepository.getWithdrawalsMetrics(withdrawalIds);
        log.info("withdrawalEntities {}", withdrawalEntities);

        var withdrawalAggregatedByMachineId = withdrawalEntities.stream()
                .collect(Collectors.toMap(WithdrawalsAggregatedMetricDto::getWithdrawalId,
                        Function.identity()));

        for (var withdrawalData : withdrawalEntities) {
            var withdrawalDto = withdrawalAggregatedByMachineId.get(withdrawalData.getWithdrawalId());
            if (withdrawalDto == null) {
                log.warn("withdrawalDto null, no gauge withdrawalData {}", withdrawalData);
                break;
            }

            gauge(machinesFailedCountMap, Metric.MACHINES_FAILED_COUNT, getWithdrawalMetricId(withdrawalData),
                    getWithdrawalTags(withdrawalData));
        }

        var invoiceIds = machinesFailedData.stream()
                .filter(i -> i.getMachineNs().contains(INVOICE))
                .map(MachinesFailedData::getMachineId)
                .limit(10)
                .toList();
        log.info("invoiceIds {}", invoiceIds);
        var invoiceEntities = paymentRepository.getPaymentsStatusMetrics(invoiceIds);
        log.info("invoiceEntities {}", invoiceEntities);

        var invoiceAggregatedByMachineId = invoiceEntities.stream()
                .collect(Collectors.toMap(PaymentsAggregatedMetricDto::getInvoiceId,
                        Function.identity()));

        for (var invoiceData : invoiceEntities) {
            var invoiceDto = invoiceAggregatedByMachineId.get(invoiceData.getInvoiceId());
            if (invoiceDto == null) {
                log.warn("invoiceDto null, no gauge invoiceData {}", invoiceData);
            }

            gauge(machinesFailedCountMap, Metric.MACHINES_FAILED_COUNT, getInvoiceMetricId(invoiceData),
                    getInvoiceTags(invoiceData));
        }
    }

    private void gauge(Map<String, Double> storage, Metric metric, String id, Tags tags) {
        if (!storage.containsKey(id)) {
            var gauge = Gauge.builder(metric.getName(), storage, map -> map.get(id))
                    .description(metric.getDescription())
                    .tags(tags);
            meterRegistryService.registry(gauge);
        }
        storage.put(id, storage.getOrDefault(id, 0.0) + 1);

    }

    private Tags getWithdrawalTags(WithdrawalsAggregatedMetricDto withdrawalData) {
        return Tags.of(
                Tag.of("machine_type", withdrawalData.getWithdrawalId()),
                Tag.of("provider_id", withdrawalData.getProviderId()),
                Tag.of("provider_name", withdrawalData.getProviderName()),
                Tag.of("currency_id", withdrawalData.getCurrencyCode())
        );
    }

    private Tags getInvoiceTags(PaymentsAggregatedMetricDto paymentData) {
        return Tags.of(
                Tag.of("machine_type", paymentData.getInvoiceId()),
                Tag.of("provider_id", paymentData.getProviderId()),
                Tag.of("provider_name", paymentData.getProviderName()),
                Tag.of("currency_id", paymentData.getCurrencyCode())
        );
    }

    private String getWithdrawalMetricId (WithdrawalsAggregatedMetricDto withdrawalData) {
        return String.format("%s:%s:%s:%s",
                withdrawalData.getWithdrawalId(),
                withdrawalData.getProviderId(),
                withdrawalData.getProviderName(),
                withdrawalData.getCurrencyCode());
    }

    private String getInvoiceMetricId (PaymentsAggregatedMetricDto paymentData) {
        return String.format("%s:%s:%s:%s",
                paymentData.getInvoiceId(),
                paymentData.getProviderId(),
                paymentData.getProviderName(),
                paymentData.getCurrencyCode());
    }
}
