package dev.vality.exporter.tech.metrics.model;

import io.micrometer.core.instrument.Tag;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomTag {

    public static final String API_TYPE_TAG = "api_type";
    public static final String HTTP_CODE_TAG = "http_code";
    public static final String URL_TAG = "url";

    public static final String MACHINE_TYPE_TAG = "machine_type";
    public static final String PROVIDER_ID_TAG = "provider_id";
    public static final String PROVIDER_NAME_TAG = "provider_name";
    public static final String CURRENCY_ID_TAG = "currency_id";

    public static Tag apiType(String apiType) {
        return Tag.of(API_TYPE_TAG, apiType);
    }

    public static Tag httpCode(String httpCode) {
        return Tag.of(HTTP_CODE_TAG, httpCode);
    }

    public static Tag url(String url) {
        return Tag.of(URL_TAG, url);
    }

    public static Tag machineType(String machineType) {
        return Tag.of(MACHINE_TYPE_TAG, machineType);
    }

    public static Tag providerId(String providerId) {
        return Tag.of(PROVIDER_ID_TAG, providerId);
    }

    public static Tag providerName(String providerName) {
        return Tag.of(PROVIDER_NAME_TAG, providerName);
    }

    public static Tag currencyId(String currencyId) {
        return Tag.of(CURRENCY_ID_TAG, currencyId);
    }

}
