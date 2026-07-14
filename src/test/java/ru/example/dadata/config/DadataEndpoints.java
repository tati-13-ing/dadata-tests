package ru.example.dadata.config;

public final class DadataEndpoints {

    public static final String IP_LOCATE_ADDRESS =
            ConfigReader.get(
                    "endpoint.ip-locate"
            );

    public static final String SUGGEST_ADDRESS =
            ConfigReader.get(
                    "endpoint.suggest-address"
            );

    public static final String FIND_PARTY_BY_ID =
            ConfigReader.get(
                    "endpoint.find-party"
            );

    public static final String FIND_BANK_BY_ID =
            ConfigReader.get(
                    "endpoint.find-bank"
            );

    private DadataEndpoints() {
    }
}