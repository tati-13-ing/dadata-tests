package ru.example.dadata.config;

public final class DadataConfig {

    private static final String API_KEY_ENV_NAME =
            "DADATA_API_KEY";

    private DadataConfig() {
    }

    public static String getBaseUrl() {
        return ConfigReader.get("base.url");
    }
    public static int getConnectionTimeoutMs() {
        return ConfigReader.getInt(
                "http.connection-timeout-ms"
        );
    }
    public static int getSocketTimeoutMs() {
        return ConfigReader.getInt(
                "http.socket-timeout-ms"
        );
    }
    public static String getApiKey() {
        String apiKey =
                System.getenv(API_KEY_ENV_NAME);

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Не задана переменная окружения "
                            + API_KEY_ENV_NAME
            );
        }

        return apiKey;
    }
}