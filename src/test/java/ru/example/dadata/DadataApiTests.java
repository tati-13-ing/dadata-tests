package ru.example.dadata;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.example.dadata.model.request.AddressSuggestionRequest;
import ru.example.dadata.model.request.IdentifierRequest;
import ru.example.dadata.model.response.AddressData;
import ru.example.dadata.model.response.AddressResult;
import ru.example.dadata.model.response.AddressSuggestionResponse;
import ru.example.dadata.model.response.IpLocateResponse;
import ru.example.dadata.service.DadataService;
import ru.example.dadata.model.response.BankSearchResponse;
import ru.example.dadata.model.response.BankSuggestion;
import ru.example.dadata.model.response.PartySearchResponse;
import ru.example.dadata.model.response.PartySuggestion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

class DadataApiTests {

    private static final String IP_ADDRESS =
            "46.226.227.20";

    private static final String ADDRESS_QUERY =
            "Москва, Тверская улица, 1";

    private static final String EMPTY_QUERY =
            "";

    private static final String SHORT_QUERY =
            "#";

    private static final String IP_WITHOUT_RESULT =
            "192.0.2.1";

    private static final String INVALID_TOKEN =
            "invalid-token";

    private static final String SBERBANK_INN =
            "7707083893";

    private static final String SBERBANK_BIC =
            "044525225";

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
    @Test
    @DisplayName("POST: поиск банка по БИК")
    void shouldFindBankByBic() {
        IdentifierRequest requestBody =
                new IdentifierRequest(
                        SBERBANK_BIC
                );

        BankSearchResponse response =
                dadataService.findBankById(
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

        List<BankSuggestion> suggestions =
                response.getSuggestions();

        assertFalse(
                suggestions.isEmpty(),
                "DaData должна вернуть банк"
        );

        assertTrue(
                suggestions.stream()
                        .map(BankSuggestion::getData)
                        .filter(data -> data != null)
                        .anyMatch(data ->
                                SBERBANK_BIC.equals(
                                        data.getBic()
                                )
                        ),
                "Ответ должен содержать заданный БИК"
        );
    }
    @Test
    @DisplayName("POST: поиск организации по ИНН")
    void shouldFindPartyByInn() {
        IdentifierRequest requestBody =
                new IdentifierRequest(
                        SBERBANK_INN
                );

        PartySearchResponse response =
                dadataService.findPartyById(
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

        List<PartySuggestion> suggestions =
                response.getSuggestions();

        assertFalse(
                suggestions.isEmpty(),
                "DaData должна вернуть организацию"
        );

        assertTrue(
                suggestions.stream()
                        .map(PartySuggestion::getData)
                        .filter(data -> data != null)
                        .anyMatch(data ->
                                SBERBANK_INN.equals(
                                        data.getInn()
                                )
                        ),
                "Ответ должен содержать заданный ИНН"
        );
    }
    @Test
    @DisplayName("POST: запрос с невалидным token возвращает 403")
    void shouldRejectInvalidToken() {
        AddressSuggestionRequest requestBody =
                new AddressSuggestionRequest(
                        ADDRESS_QUERY
                );

        Response response =
                dadataService.suggestAddressWithToken(
                        requestBody,
                        INVALID_TOKEN
                );

        assertEquals(
                403,
                response.statusCode(),
                "Для невалидного token ожидается статус 403"
        );
    }
    @Test
    @DisplayName("POST: запрос без token возвращает 401")
    void shouldRejectRequestWithoutToken() {
        AddressSuggestionRequest requestBody =
                new AddressSuggestionRequest(
                        ADDRESS_QUERY
                );

        Response response =
                dadataService.suggestAddressWithoutToken(
                        requestBody
                );

        assertEquals(
                401,
                response.statusCode(),
                "Для запроса без token ожидается статус 401"
        );
    }
    @Test
    @DisplayName("POST: пустой query возвращает пустой список подсказок")
    void shouldReturnEmptySuggestionsForEmptyQuery() {
        AddressSuggestionRequest requestBody =
                new AddressSuggestionRequest(
                        EMPTY_QUERY
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

        assertTrue(
                response.getSuggestions().isEmpty(),
                "Для пустого query список suggestions должен быть пустым"
        );
    }
    @Test
    @DisplayName("POST: короткий query без совпадений возвращает пустой список")
    void shouldReturnEmptySuggestionsForShortQuery() {
        AddressSuggestionRequest requestBody =
                new AddressSuggestionRequest(
                        SHORT_QUERY
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

        assertTrue(
                response.getSuggestions().isEmpty(),
                "Для короткого query без совпадений список должен быть пустым"
        );
    }
    @Test
    @DisplayName("GET: IP без результата возвращает location = null")
    void shouldReturnNullLocationForUnknownIp() {
        IpLocateResponse response =
                dadataService.detectAddressByIp(
                        IP_WITHOUT_RESULT
                );

        assertNotNull(
                response,
                "Ответ не должен быть null"
        );

        assertNull(
                response.getLocation(),
                "Для IP без результата поле location должно быть null"
        );
    }

}