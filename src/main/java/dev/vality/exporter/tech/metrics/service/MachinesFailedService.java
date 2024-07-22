package dev.vality.exporter.tech.metrics.service;

import dev.vality.exporter.tech.metrics.model.MachinesFailedData;
import dev.vality.exporter.tech.metrics.repository.PaymentRepository;
import dev.vality.exporter.tech.metrics.repository.WithdrawalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MachinesFailedService {

    private final OpenSearchService openSearchService;
    private final PaymentRepository paymentRepository;
    private final WithdrawalRepository withdrawalRepository;
    private static final String WITHDRAWAL = "withdrawal";
    private static final String INVOICE = "invoice";


    public void registerMetrics() {
        var machinesFailedData = openSearchService.getMachinesFailedData();

        var withdrawalIds = machinesFailedData.stream()
                .filter(w -> w.getMachineNs().contains(WITHDRAWAL))
                .map(MachinesFailedData::getMachineId)
                .limit(10)
                .toList();
        log.info("withdrawalIds {}", withdrawalIds);
        var withdrawalEntities = withdrawalRepository.getWithdrawalsMetrics(withdrawalIds);
        log.info("withdrawalEntities {}", withdrawalEntities);

        var invoiceIds = machinesFailedData.stream()
                .filter(w -> w.getMachineNs().contains(INVOICE))
                .map(MachinesFailedData::getMachineId)
                .limit(10)
                .toList();
        log.info("invoiceIds {}", invoiceIds);
        var invoiceEntities = paymentRepository.getPaymentsStatusMetrics(invoiceIds);
        log.info("invoiceEntities {}", invoiceEntities);
    }
}
