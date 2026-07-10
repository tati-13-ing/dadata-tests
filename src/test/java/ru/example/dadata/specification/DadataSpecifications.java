package ru.example.dadata.specification;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.example.dadata.config.DadataConfig;

public final class DadataSpecifications {

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
                .build();
    }

    public static RequestSpecification requestSpecificationWithoutToken() {
        return baseRequestSpecificationBuilder()
                .build();
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
}