package dev.vality.exporter.tech.metrics.repository;

import dev.vality.exporter.tech.metrics.entity.PaymentsAggregatedMetricDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ComponentScan(basePackages = "dev.vality.exporter.tech.metrics.repository",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {PaymentRepository.class}))
@SqlGroup({
        @Sql(
                scripts = "classpath:schema.sql",
                config = @SqlConfig(encoding = "utf-8")
        ),
        @Sql(
                scripts = "classpath:data.sql",
                config = @SqlConfig(encoding = "utf-8")
        )
})
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void getPaymentsStatusMetricsTest() {
        List<PaymentsAggregatedMetricDto> metrics = paymentRepository.getPaymentsStatusMetrics(List.of("inv1", "inv2"));

        assertThat(metrics).isNotEmpty();
        assertThat(metrics.size()).isEqualTo(2);

        PaymentsAggregatedMetricDto metric1 = metrics.get(0);
        assertThat(metric1.getInvoiceId()).isEqualTo("inv1");
        assertThat(metric1.getProviderId()).isEqualTo("1001");
        assertThat(metric1.getProviderName()).isEqualTo("Provider 1");
        assertThat(metric1.getCurrencyCode()).isEqualTo("USD");

        PaymentsAggregatedMetricDto metric2 = metrics.get(1);
        assertThat(metric2.getInvoiceId()).isEqualTo("inv2");
        assertThat(metric2.getProviderId()).isEqualTo("1002");
        assertThat(metric2.getProviderName()).isEqualTo("Provider 2");
        assertThat(metric2.getCurrencyCode()).isEqualTo("EUR");
    }
}