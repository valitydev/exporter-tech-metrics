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

            String id = String.format("%s:%s:%s:%s",
                    withdrawalData.getWithdrawalId(),
                    withdrawalData.getProviderId(),
                    withdrawalData.getProviderName(),
                    withdrawalData.getCurrencyCode());

            machinesFailedCountMap.put(id, machinesFailedCountMap.getOrDefault(id, 0.0) + 1);

            Tags tags = Tags.of(
                    Tag.of("withdrawalId", withdrawalData.getWithdrawalId()),
                    Tag.of("providerId", withdrawalData.getProviderId()),
                    Tag.of("providerName", withdrawalData.getProviderName()),
                    Tag.of("currencyCode", withdrawalData.getCurrencyCode())
            );

            var gauge = Gauge.builder(Metric.MACHINES_FAILED_COUNT.getName(), machinesFailedCountMap, map -> map.get(id))
                    .description(Metric.MACHINES_FAILED_COUNT.getDescription())
                    .tags(tags);

            meterRegistryService.registry(gauge);
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

    }

}
