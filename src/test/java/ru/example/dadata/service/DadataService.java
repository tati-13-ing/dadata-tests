package ru.example.dadata.service;

import io.restassured.http.ContentType;
import ru.example.dadata.model.request.AddressSuggestionRequest;
import ru.example.dadata.model.response.AddressSuggestionResponse;
import ru.example.dadata.model.response.IpLocateResponse;

import static io.restassured.RestAssured.given;
import static ru.example.dadata.config.DadataEndpoints.IP_LOCATE_ADDRESS;
import static ru.example.dadata.config.DadataEndpoints.SUGGEST_ADDRESS;
import static ru.example.dadata.specification.DadataSpecifications.requestSpecification;
import static ru.example.dadata.specification.DadataSpecifications.successfulResponseSpecification;

public class DadataService {

    public IpLocateResponse detectAddressByIp(
            String ipAddress
    ) {
        return given()
                .spec(requestSpecification())
                .queryParam("ip", ipAddress)
                .when()
                .get(IP_LOCATE_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(successfulResponseSpecification())
                .extract()
                .as(IpLocateResponse.class);
    }

    public AddressSuggestionResponse suggestAddress(
            AddressSuggestionRequest requestBody
    ) {
        return given()
                .spec(requestSpecification())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(SUGGEST_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(successfulResponseSpecification())
                .extract()
                .as(AddressSuggestionResponse.class);
    }
}