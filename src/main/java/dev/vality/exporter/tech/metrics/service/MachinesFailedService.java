package dev.vality.exporter.tech.metrics.service;

import dev.vality.exporter.tech.metrics.entity.PaymentsAggregatedMetricDto;
import dev.vality.exporter.tech.metrics.entity.WithdrawalsAggregatedMetricDto;
import dev.vality.exporter.tech.metrics.model.MachinesFailedData;
import dev.vality.exporter.tech.metrics.model.Metric;
import dev.vality.exporter.tech.metrics.repository.PaymentRepository;
import dev.vality.exporter.tech.metrics.repository.WithdrawalRepository;
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
    private static final String AFTER_SLASH = "/\\d+$";


    public void registerMetrics() {
        var machinesFailedData = openSearchService.getMachinesFailedData();
        log.debug("machinesFailedData size {}", machinesFailedData.size());

        if (machinesFailedData.isEmpty()) {
            return;
        }

        var withdrawalIds = machinesFailedData.stream()
                .filter(w -> w.getMachineNs().contains(WITHDRAWAL))
                .map(MachinesFailedData::getMachineId)
                .map(id -> id.replaceAll(AFTER_SLASH, ""))
                .toList();
        log.debug("withdrawalIds size {}, first 10 withdrawalIds {}", withdrawalIds.size(),
                withdrawalIds.stream().limit(10).collect(Collectors.toList()));
        var withdrawalEntities = withdrawalRepository.getWithdrawalsMetrics(withdrawalIds);
        log.debug("withdrawalEntities size {}, first 10 withdrawalEntities {}", withdrawalEntities.size(),
                withdrawalEntities.stream().limit(10).collect(Collectors.toList()));

        var withdrawalAggregatedByMachineId = withdrawalEntities.stream()
                .collect(Collectors.toMap(WithdrawalsAggregatedMetricDto::getWithdrawalId,
                        Function.identity()));

        for (var withdrawalData : withdrawalEntities) {
            var withdrawalDto = withdrawalAggregatedByMachineId.get(withdrawalData.getWithdrawalId());
            if (withdrawalDto == null) {
                log.warn("withdrawalDto null, no gauge withdrawalData {}", withdrawalData);
                break;
            }

            meterRegistryService.gauge(machinesFailedCountMap, Metric.MACHINES_FAILED_COUNT,
                    getMetricId(WITHDRAWAL, withdrawalData.getProviderId(), withdrawalData.getProviderName(),
                            withdrawalData.getCurrencyCode()),
                    getTags(WITHDRAWAL, withdrawalData.getProviderId(), withdrawalData.getProviderName(),
                            withdrawalData.getCurrencyCode()));
        }

        var invoiceIds = machinesFailedData.stream()
                .filter(i -> i.getMachineNs().contains(INVOICE))
                .map(MachinesFailedData::getMachineId)
                .toList();
        log.debug("invoiceIds size {}, first 10 invoiceIds {}", invoiceIds.size(),
                invoiceIds.stream().limit(10).collect(Collectors.toList()));
        var invoiceEntities = paymentRepository.getPaymentsStatusMetrics(invoiceIds);
        log.debug("invoiceEntities size {}, first 10 invoiceEntities {}", invoiceEntities,
                invoiceEntities.stream().limit(10).collect(Collectors.toList()));

        var invoiceAggregatedByMachineId = invoiceEntities.stream()
                .collect(Collectors.toMap(PaymentsAggregatedMetricDto::getInvoiceId,
                        Function.identity()));

        for (var invoiceData : invoiceEntities) {
            var invoiceDto = invoiceAggregatedByMachineId.get(invoiceData.getInvoiceId());
            if (invoiceDto == null) {
                log.warn("invoiceDto null, no gauge invoiceData {}", invoiceData);
            }

            meterRegistryService.gauge(machinesFailedCountMap, Metric.MACHINES_FAILED_COUNT,
                    getMetricId(INVOICE, invoiceData.getProviderId(), invoiceData.getProviderName(),
                            invoiceData.getCurrencyCode()),
                    getTags(INVOICE, invoiceData.getProviderId(), invoiceData.getProviderName(),
                            invoiceData.getCurrencyCode()));
        }

        if (log.isDebugEnabled()) {
            var registeredMetricsSize =
                    meterRegistryService.getRegisteredMetricsSize(Metric.MACHINES_FAILED_COUNT.getName());
            log.debug("Limits metrics have been registered to 'prometheus', " +
                    "registeredMetricsSize = {}, machinesFailedDataSize = {}",
                    registeredMetricsSize, machinesFailedData.size());
        }
    }

    private Tags getTags(String machineType, String providerId, String providerName, String currencyCode) {
        return Tags.of(
                Tag.of("machine_type", machineType),
                Tag.of("provider_id", providerId),
                Tag.of("provider_name", providerName),
                Tag.of("currency_id", currencyCode)
        );
    }

    private String getMetricId(String machineType, String providerId, String providerName, String currencyCode) {
        return String.format("%s:%s:%s:%s",
                machineType,
                providerId,
                providerName,
                currencyCode);
    }
}
