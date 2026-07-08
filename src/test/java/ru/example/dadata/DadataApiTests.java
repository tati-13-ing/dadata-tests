package ru.example.dadata;

import io.restassured.http.ContentType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.dadata.dto.AddressData;
import ru.example.dadata.dto.AddressResult;
import ru.example.dadata.dto.AddressSuggestionRequest;
import ru.example.dadata.dto.AddressSuggestionResponse;
import ru.example.dadata.dto.IpLocateResponse;
import ru.example.dadata.config.DadataConfig;

import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.example.dadata.config.DadataConfig.BASE_URL;
import static ru.example.dadata.constant.DadataEndpoints.IP_LOCATE_ADDRESS;
import static ru.example.dadata.constant.DadataEndpoints.SUGGEST_ADDRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DadataApiTests {


    private static final String IP_ADDRESS =
            "46.226.227.20";

    private RequestSpecification requestSpecification;

    @BeforeEach
    void setUp() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader(
                        "Authorization",
                        "Token " + DadataConfig.getApiKey()
                )
                .setAccept(ContentType.JSON)
                .build();
    }

    @Test
    @DisplayName("GET: определение города по IP-адресу")
    void shouldDetectAddressByIp() {
        IpLocateResponse response = given()
                .spec(requestSpecification)
                .queryParam("ip", IP_ADDRESS)
                .when()
                .get(IP_LOCATE_ADDRESS)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(IpLocateResponse.class);

        assertNotNull(
                response,
                "Ответ не должен быть null"
        );

        assertNotNull(
                response.getLocation(),
                "Поле location не должно быть null"
        );

        AddressResult location = response.getLocation();

        assertNotNull(
                location.getData(),
                "В location должен присутствовать объект data"
        );

        AddressData data = location.getData();

        assertEquals(
                "RU",
                data.getCountryIsoCode(),
                "Код страны должен быть RU"
        );

        assertNotNull(
                data.getCity(),
                "Название города не должно быть null"
        );

        assertFalse(
                data.getCity().isBlank(),
                "Название города не должно быть пустым"
        );
    }

    @Test
    @DisplayName("POST: получение подсказок по адресу")
    void shouldSuggestAddress() {
        AddressSuggestionRequest requestBody =
                new AddressSuggestionRequest(
                        "Москва, Тверская улица, 1"
                );

        AddressSuggestionResponse response = given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(SUGGEST_ADDRESS)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(AddressSuggestionResponse.class);

        assertNotNull(
                response,
                "Ответ не должен быть null"
        );

        assertNotNull(
                response.getSuggestions(),
                "Поле suggestions не должно быть null"
        );

        List<AddressResult> suggestions =
                response.getSuggestions();

        assertFalse(
                suggestions.isEmpty(),
                "DaData должна вернуть хотя бы одну подсказку"
        );

        AddressResult firstSuggestion =
                suggestions.get(0);

        assertNotNull(
                firstSuggestion,
                "Первая подсказка не должна быть null"
        );

        assertNotNull(
                firstSuggestion.getValue(),
                "Поле value не должно быть null"
        );

        assertTrue(
                firstSuggestion.getValue().contains("Москва"),
                "Первая подсказка должна содержать слово Москва"
        );

        assertNotNull(
                firstSuggestion.getData(),
                "В подсказке должен присутствовать объект data"
        );

        AddressData data = firstSuggestion.getData();

        assertEquals(
                "RU",
                data.getCountryIsoCode(),
                "Код страны должен быть RU"
        );
    }
}