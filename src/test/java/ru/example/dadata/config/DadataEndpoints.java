package ru.example.dadata.config;

public final class DadataEndpoints {

    public static final String IP_LOCATE_ADDRESS =
            "/suggestions/api/4_1/rs/iplocate/address";

    public static final String SUGGEST_ADDRESS =
            "/suggestions/api/4_1/rs/suggest/address";

    public static final String FIND_PARTY_BY_ID =
            "/suggestions/api/4_1/rs/findById/party";

    public static final String FIND_BANK_BY_ID =
            "/suggestions/api/4_1/rs/findById/bank";

    private DadataEndpoints() {
    }
}