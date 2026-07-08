package ru.example.dadata.config;

public final class DadataConfig {

    public static final String BASE_URL =
            "https://suggestions.dadata.ru";

    private static final String API_KEY_ENV_NAME =
            "DADATA_API_KEY";

    private DadataConfig() {
    }

    public static String getApiKey() {
        String apiKey = System.getenv(API_KEY_ENV_NAME);

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Не задана переменная окружения " + API_KEY_ENV_NAME
            );
        }

        return apiKey;
    }
}