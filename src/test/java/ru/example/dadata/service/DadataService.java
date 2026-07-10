package ru.example.dadata.service;

import io.restassured.response.Response;
import ru.example.dadata.model.request.AddressSuggestionRequest;
import ru.example.dadata.model.request.IdentifierRequest;
import ru.example.dadata.model.response.AddressSuggestionResponse;
import ru.example.dadata.model.response.BankSearchResponse;
import ru.example.dadata.model.response.IpLocateResponse;
import ru.example.dadata.model.response.PartySearchResponse;

import static io.restassured.RestAssured.given;
import static ru.example.dadata.config.DadataEndpoints.FIND_BANK_BY_ID;
import static ru.example.dadata.config.DadataEndpoints.FIND_PARTY_BY_ID;
import static ru.example.dadata.config.DadataEndpoints.IP_LOCATE_ADDRESS;
import static ru.example.dadata.config.DadataEndpoints.SUGGEST_ADDRESS;
import static ru.example.dadata.specification.DadataSpecifications.requestSpecification;
import static ru.example.dadata.specification.DadataSpecifications.successfulResponseSpecification;
import static ru.example.dadata.specification.DadataSpecifications.requestSpecificationWithToken;
import static ru.example.dadata.specification.DadataSpecifications.requestSpecificationWithoutToken;

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
                .body(requestBody)
                .when()
                .post(SUGGEST_ADDRESS)
                .then()
                .log().ifValidationFails()
                .spec(successfulResponseSpecification())
                .extract()
                .as(AddressSuggestionResponse.class);
    }
    public Response suggestAddressWithToken(
            AddressSuggestionRequest requestBody,
            String token
    ) {
        return given()
                .spec(requestSpecificationWithToken(token))
                .body(requestBody)
                .when()
                .post(SUGGEST_ADDRESS);
    }

    public Response suggestAddressWithoutToken(
            AddressSuggestionRequest requestBody
    ) {
        return given()
                .spec(requestSpecificationWithoutToken())
                .body(requestBody)
                .when()
                .post(SUGGEST_ADDRESS);
    }

    public PartySearchResponse findPartyById(
            IdentifierRequest requestBody
    ) {
        return given()
                .spec(requestSpecification())
                .body(requestBody)
                .when()
                .post(FIND_PARTY_BY_ID)
                .then()
                .log().ifValidationFails()
                .spec(successfulResponseSpecification())
                .extract()
                .as(PartySearchResponse.class);
    }

    public BankSearchResponse findBankById(
            IdentifierRequest requestBody
    ) {
        return given()
                .spec(requestSpecification())
                .body(requestBody)
                .when()
                .post(FIND_BANK_BY_ID)
                .then()
                .log().ifValidationFails()
                .spec(successfulResponseSpecification())
                .extract()
                .as(BankSearchResponse.class);
    }
}