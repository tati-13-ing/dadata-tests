package ru.example.dadata;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.dadata.dto.AddressSuggestionRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

class DadataApiTests {

    private static final String BASE_URL =
            "https://suggestions.dadata.ru";

    private static final String IP_ADDRESS =
            "46.226.227.20";

    private static String apiKey;

    @BeforeAll
    static void setUp() {
        apiKey = System.getenv("DADATA_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Не задана переменная окружения DADATA_API_KEY"
            );
        }

        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("GET: определение города по IP-адресу")
    void shouldDetectAddressByIp() {
        given()
                .header("Authorization", "Token " + apiKey)
                .accept(ContentType.JSON)
                .queryParam("ip", IP_ADDRESS)
                .when()
                .get("/suggestions/api/4_1/rs/iplocate/address")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("location", notNullValue())
                .body(
                        "location.data.country_iso_code",
                        equalTo("RU")
                )
                .body(
                        "location.data.city",
                        not(emptyOrNullString())
                );
    }

    @Test
    @DisplayName("POST: получение подсказок по адресу")
    void shouldSuggestAddress() {
        AddressSuggestionRequest requestBody =
                new AddressSuggestionRequest(
                        "Москва, Тверская улица, 1"
                );

        given()
                .header("Authorization", "Token " + apiKey)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/suggestions/api/4_1/rs/suggest/address")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("suggestions", not(empty()))
                .body(
                        "suggestions[0].value",
                        containsString("Москва")
                )
                .body(
                        "suggestions[0].data.country_iso_code",
                        equalTo("RU")
                );
    }
}