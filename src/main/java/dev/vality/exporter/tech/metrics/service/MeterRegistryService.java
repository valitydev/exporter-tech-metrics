package dev.vality.exporter.tech.metrics.service;

import dev.vality.exporter.tech.metrics.model.Metric;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MeterRegistryService {

    private final MeterRegistry meterRegistry;

    public <T> void registry(Gauge.Builder<T> builder) {
        builder.register(meterRegistry);
    }

    public long getRegisteredMetricsSize(String name) {
        return meterRegistry.getMeters().stream()
                .filter(meter -> meter.getId().getName().equals(name))
                .filter(Gauge.class::isInstance)
                .count();
    }

    public void gauge(Map<String, Double> storage, Metric metric, String id, Tags tags) {
        if (!storage.containsKey(id)) {
            var gauge = Gauge.builder(metric.getName(), storage, map -> map.get(id))
                    .description(metric.getDescription())
                    .tags(tags);
            registry(gauge);
        }
        storage.put(id, storage.getOrDefault(id, 0.0) + 1);
    }
}
