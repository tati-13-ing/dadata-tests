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
        return new RequestSpecBuilder()
                .setBaseUri(DadataConfig.getBaseUrl())
                .addHeader(
                        "Authorization",
                        "Token " + DadataConfig.getApiKey()
                )
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification
    successfulResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }
}