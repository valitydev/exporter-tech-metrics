package dev.vality.exporter.tech.metrics.service;

import dev.vality.exporter.tech.metrics.config.OpenSearchProperties;
import dev.vality.exporter.tech.metrics.model.HttpCodeData;
import dev.vality.exporter.tech.metrics.model.MachinesFailedData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.mapping.FieldType;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch._types.query_dsl.MatchPhraseQuery;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.RangeQuery;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("LineLength")
public class OpenSearchService {

    private static final String TIMESTAMP = "@timestamp";
    private static final String DATE_TIME = "date_time";
    private static final String STRICT_DATE_OPTIONAL_TIME = "strict_date_optional_time";
    private static final String STATUS = "status";
    private static final String SERVICE = "service";
    private static final String INGRESS_CONTROLLER = "ingress-controller";
    private static final String REQUEST = "request";
    private static final String V2 = "v2";
    private static final String ANAPI = "anapi";
    private static final String ANALYTICS = "analytics";
    private static final String WALLET = "wallet";
    private static final String PRIVDOC = "privdoc";
    private static final String PAYRES = "payres";
    private static final String MACHINEGUN = "machinegun";
    private static final String SEVERITY = "@severity";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String MACHINE_FAILED = "machine failed";
    private static final String STATUS_START = "100";
    private static final String STATUS_END = "599";

    private final OpenSearchProperties openSearchProperties;
    private final OpenSearchClient openSearchClient;

    @Value("${interval.time}")
    private String intervalTime;

    @SneakyThrows
    public List<HttpCodeData> getCapiHttpCodeData() {
        return openSearchClient.search(s -> s
                                .size(10000)
                                .index(openSearchProperties.getIndex())
                                .sort(builder -> builder
                                        .field(builder1 -> builder1
                                                .field(TIMESTAMP)
                                                .order(SortOrder.Desc)
                                                .unmappedType(FieldType.Boolean)))
                                .docvalueFields(builder -> builder
                                        .field(TIMESTAMP)
                                        .format(DATE_TIME))
                                .query(builder -> builder
                                        .bool(builder1 -> builder1
                                                .filter(new RangeQuery.Builder()
                                                                .field(TIMESTAMP)
                                                                .gte(JsonData.of(
                                                                        String.format("now-%ss", intervalTime)))
                                                                .format(STRICT_DATE_OPTIONAL_TIME)
                                                                .build()
                                                                ._toQuery(),
                                                        new BoolQuery.Builder()
                                                                .should(new Query(new MatchPhraseQuery.Builder()
                                                                                .field(SERVICE)
                                                                                .query(INGRESS_CONTROLLER)
                                                                                .build()),
                                                                        new Query(new MatchPhraseQuery.Builder()
                                                                                .field(REQUEST)
                                                                                .query(V2)
                                                                                .build()),
                                                                        new RangeQuery.Builder()
                                                                                .field(STATUS)
                                                                                .gte(JsonData.of(STATUS_START))
                                                                                .lte(JsonData.of(STATUS_END))
                                                                                .build()
                                                                                ._toQuery())
                                                                .mustNot(new BoolQuery.Builder()
                                                                        .minimumShouldMatch("1")
                                                                        .should(new Query(new MatchPhraseQuery.Builder()
                                                                                        .field(REQUEST)
                                                                                        .query(ANAPI)
                                                                                        .build()),
                                                                                new Query(new MatchPhraseQuery.Builder()
                                                                                        .field(REQUEST)
                                                                                        .query(ANALYTICS)
                                                                                        .build()))
                                                                        .build()
                                                                        ._toQuery())
                                                                .build()
                                                                ._toQuery()))),
                        HttpCodeData.class)
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<HttpCodeData> getWapiHttpCodeData() {
        return openSearchClient.search(s -> s
                                .size(10000)
                                .index(openSearchProperties.getIndex())
                                .sort(builder -> builder
                                        .field(builder1 -> builder1
                                                .field(TIMESTAMP)
                                                .order(SortOrder.Desc)
                                                .unmappedType(FieldType.Boolean)))
                                .docvalueFields(builder -> builder
                                        .field(TIMESTAMP)
                                        .format(DATE_TIME))
                                .query(builder -> builder
                                        .bool(builder1 -> builder1
                                                .filter(new RangeQuery.Builder()
                                                                .field(TIMESTAMP)
                                                                .gte(JsonData.of(
                                                                        String.format("now-%ss", intervalTime)))
                                                                .format(STRICT_DATE_OPTIONAL_TIME)
                                                                .build()
                                                                ._toQuery(),
                                                        new BoolQuery.Builder()
                                                                .should(new Query(new MatchPhraseQuery.Builder()
                                                                                .field(SERVICE)
                                                                                .query(INGRESS_CONTROLLER)
                                                                                .build()),
                                                                        new RangeQuery.Builder()
                                                                                .field(STATUS)
                                                                                .gte(JsonData.of(STATUS_START))
                                                                                .lte(JsonData.of(STATUS_END))
                                                                                .build()
                                                                                ._toQuery(),
                                                                        new BoolQuery.Builder()
                                                                                .minimumShouldMatch("1")
                                                                                .should(new Query(new MatchPhraseQuery.Builder()
                                                                                                .field(REQUEST)
                                                                                                .query(WALLET)
                                                                                                .build()),
                                                                                        new Query(new MatchPhraseQuery.Builder()
                                                                                                .field(REQUEST)
                                                                                                .query(PRIVDOC)
                                                                                                .build()),
                                                                                        new Query(new MatchPhraseQuery.Builder()
                                                                                                .field(REQUEST)
                                                                                                .query(PAYRES)
                                                                                                .build()))
                                                                                .build()
                                                                                ._toQuery())
                                                                .mustNot(new BoolQuery.Builder()
                                                                        .minimumShouldMatch("1")
                                                                        .should(new Query(new MatchPhraseQuery.Builder()
                                                                                        .field(REQUEST)
                                                                                        .query(ANAPI)
                                                                                        .build()),
                                                                                new Query(new MatchPhraseQuery.Builder()
                                                                                        .field(REQUEST)
                                                                                        .query(ANALYTICS)
                                                                                        .build()))
                                                                        .build()
                                                                        ._toQuery())
                                                                .build()
                                                                ._toQuery()))),
                        HttpCodeData.class)
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<MachinesFailedData> getMachinesFailedData() {
        return openSearchClient.search(s -> s
                                .size(10000)
                                .index(openSearchProperties.getIndex())
                                .sort(builder -> builder
                                        .field(builder1 -> builder1
                                                .field(TIMESTAMP)
                                                .order(SortOrder.Desc)
                                                .unmappedType(FieldType.Boolean)))
                                .docvalueFields(builder -> builder
                                        .field(TIMESTAMP)
                                        .format(DATE_TIME))
                                .query(builder -> builder
                                        .bool(builder1 -> builder1
                                                .filter(new RangeQuery.Builder()
                                                                .field(TIMESTAMP)
                                                                .gte(JsonData.of(
                                                                        String.format("now-%ss", intervalTime)))
                                                                .format(STRICT_DATE_OPTIONAL_TIME)
                                                                .build()
                                                                ._toQuery(),
                                                        new BoolQuery.Builder()
                                                                .should(new Query(new MatchPhraseQuery.Builder()
                                                                                .field(SERVICE)
                                                                                .query(MACHINEGUN)
                                                                                .build()),
                                                                        new Query(new MatchPhraseQuery.Builder()
                                                                                .field(SEVERITY)
                                                                                .query(ERROR)
                                                                                .build()),
                                                                        new Query(new MatchPhraseQuery.Builder()
                                                                                .field(MESSAGE)
                                                                                .query(MACHINE_FAILED)
                                                                                .build()))
                                                                .build()
                                                                ._toQuery()))),
                        MachinesFailedData.class)
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
