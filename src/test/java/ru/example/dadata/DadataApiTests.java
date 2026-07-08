package ru.example.dadata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.dadata.model.request.AddressSuggestionRequest;
import ru.example.dadata.model.response.AddressData;
import ru.example.dadata.model.response.AddressResult;
import ru.example.dadata.model.response.AddressSuggestionResponse;
import ru.example.dadata.model.response.IpLocateResponse;
import ru.example.dadata.service.DadataService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DadataApiTests {

    private static final String IP_ADDRESS =
            "46.226.227.20";

    private static final String ADDRESS_QUERY =
            "Москва, Тверская улица, 1";

    private final DadataService dadataService =
            new DadataService();

    @Test
    @DisplayName("GET: определение города по IP-адресу")
    void shouldDetectAddressByIp() {
        IpLocateResponse response =
                dadataService.detectAddressByIp(
                        IP_ADDRESS
                );

        assertNotNull(
                response,
                "Ответ не должен быть null"
        );

        assertNotNull(
                response.getLocation(),
                "Поле location не должно быть null"
        );

        AddressResult location =
                response.getLocation();

        assertNotNull(
                location.getData(),
                "В location должен присутствовать объект data"
        );

        AddressData data =
                location.getData();

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
                        ADDRESS_QUERY
                );

        AddressSuggestionResponse response =
                dadataService.suggestAddress(
                        requestBody
                );

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
                firstSuggestion
                        .getValue()
                        .contains("Москва"),
                "Первая подсказка должна содержать слово Москва"
        );

        assertNotNull(
                firstSuggestion.getData(),
                "В подсказке должен присутствовать объект data"
        );

        AddressData data =
                firstSuggestion.getData();

        assertEquals(
                "RU",
                data.getCountryIsoCode(),
                "Код страны должен быть RU"
        );
    }
}