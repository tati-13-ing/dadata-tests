package ru.example.dadata.specification;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.example.dadata.config.DadataConfig;

public final class DadataSpecifications {
    private static final String CONNECTION_TIMEOUT_PARAMETER =
            "http.connection.timeout";

    private static final String SOCKET_TIMEOUT_PARAMETER =
            "http.socket.timeout";

    private DadataSpecifications() {
    }

    public static RequestSpecification requestSpecification() {
        return requestSpecificationWithToken(
                DadataConfig.getApiKey()
        );
    }

    public static RequestSpecification requestSpecificationWithToken(
            String token
    ) {
        return baseRequestSpecificationBuilder()
                .addHeader(
                        "Authorization",
                        "Token " + token
                )
                .build()
                .config(restAssuredConfig());
    }

    public static RequestSpecification requestSpecificationWithoutToken() {
        return baseRequestSpecificationBuilder()
                .build()
                .config(restAssuredConfig());
    }

    private static RequestSpecBuilder baseRequestSpecificationBuilder() {
        return new RequestSpecBuilder()
                .setBaseUri(DadataConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);
    }

    public static ResponseSpecification
    successfulResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }
    private static RestAssuredConfig restAssuredConfig() {
        int connectionTimeoutMs =
                DadataConfig.getConnectionTimeoutMs();

        int socketTimeoutMs =
                DadataConfig.getSocketTimeoutMs();

        return RestAssuredConfig.config()
                .httpClient(
                        HttpClientConfig
                                .httpClientConfig()
                                .setParam(
                                        CONNECTION_TIMEOUT_PARAMETER,
                                        connectionTimeoutMs
                                )
                                .setParam(
                                        SOCKET_TIMEOUT_PARAMETER,
                                        socketTimeoutMs
                                )
                );
    }
}