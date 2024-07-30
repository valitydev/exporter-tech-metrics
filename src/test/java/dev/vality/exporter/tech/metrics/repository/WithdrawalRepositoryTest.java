package dev.vality.exporter.tech.metrics.repository;

import dev.vality.exporter.tech.metrics.entity.WithdrawalsAggregatedMetricDto;
import org.assertj.core.api.AssertionsForClassTypes;
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
class WithdrawalRepositoryTest {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Test
    void getWithdrawalsMetricsTest() {
        List<WithdrawalsAggregatedMetricDto> metrics = withdrawalRepository.getWithdrawalsMetrics(List.of("w1", "w2"));

        assertThat(metrics).isNotEmpty();
        AssertionsForClassTypes.assertThat(metrics.size()).isEqualTo(2);

        WithdrawalsAggregatedMetricDto metric1 = metrics.get(0);
        AssertionsForClassTypes.assertThat(metric1.getWithdrawalId()).isEqualTo("w1");
        AssertionsForClassTypes.assertThat(metric1.getProviderId()).isEqualTo("1001");
        AssertionsForClassTypes.assertThat(metric1.getProviderName()).isEqualTo("Provider 1");
        AssertionsForClassTypes.assertThat(metric1.getCurrencyCode()).isEqualTo("USD");

        WithdrawalsAggregatedMetricDto metric2 = metrics.get(1);
        AssertionsForClassTypes.assertThat(metric2.getWithdrawalId()).isEqualTo("w2");
        AssertionsForClassTypes.assertThat(metric2.getProviderId()).isEqualTo("1002");
        AssertionsForClassTypes.assertThat(metric2.getProviderName()).isEqualTo("Provider 2");
        AssertionsForClassTypes.assertThat(metric2.getCurrencyCode()).isEqualTo("EUR");
    }

}