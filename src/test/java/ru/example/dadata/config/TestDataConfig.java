package ru.example.dadata.config;

public final class TestDataConfig {

    private TestDataConfig() {
    }

    public static String getIpAddress() {
        return ConfigReader.get(
                "test.ip-address"
        );
    }

    public static String getAddressQuery() {
        return ConfigReader.get(
                "test.address-query"
        );
    }

    public static String getEmptyQuery() {
        return ConfigReader.get(
                "test.empty-query"
        );
    }

    public static String getShortQuery() {
        return ConfigReader.get(
                "test.short-query"
        );
    }

    public static String getIpWithoutResult() {
        return ConfigReader.get(
                "test.ip-without-result"
        );
    }

    public static String getInvalidToken() {
        return ConfigReader.get(
                "test.invalid-token"
        );
    }

    public static String getSberbankInn() {
        return ConfigReader.get(
                "test.sberbank-inn"
        );
    }

    public static String getSberbankBic() {
        return ConfigReader.get(
                "test.sberbank-bic"
        );
    }

    public static String getExpectedCountryCode() {
        return ConfigReader.get(
                "expected.country-code"
        );
    }

    public static String getExpectedAddressFragment() {
        return ConfigReader.get(
                "expected.address-fragment"
        );
    }

    public static int getInvalidTokenStatus() {
        return ConfigReader.getInt(
                "expected.invalid-token-status"
        );
    }

    public static int getMissingTokenStatus() {
        return ConfigReader.getInt(
                "expected.missing-token-status"
        );
    }
}