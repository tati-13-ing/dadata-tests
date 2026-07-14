package ru.example.dadata;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import ru.example.dadata.config.TestDataConfig;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

class DadataApiTests {

    private final DadataService dadataService =
            new DadataService();

    @Test
    @DisplayName("GET: определение города по IP-адресу")
    void shouldDetectAddressByIp() {
        IpLocateResponse response =
                dadataService.detectAddressByIp(
                        TestDataConfig.getIpAddress()
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
                TestDataConfig
                        .getExpectedCountryCode(),
                data.getCountryIsoCode(),
                "Код страны должен соответствовать ожидаемому"
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
                        TestDataConfig.getAddressQuery()
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
                        .contains(
                                TestDataConfig
                                        .getExpectedAddressFragment()
                        ),
                "Первая подсказка должна содержать слово Москва"
        );

        assertNotNull(
                firstSuggestion.getData(),
                "В подсказке должен присутствовать объект data"
        );

        AddressData data =
                firstSuggestion.getData();

        assertEquals(
                TestDataConfig
                        .getExpectedCountryCode(),
                data.getCountryIsoCode(),
                "Код страны должен соответствовать ожидаемому"
        );
    }
    @Test
    @DisplayName("POST: поиск банка по БИК")
    void shouldFindBankByBic() {
        IdentifierRequest requestBody =
                new IdentifierRequest(
                        TestDataConfig.getSberbankBic()
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
                                TestDataConfig
                                        .getSberbankBic()
                                        .equals(
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
                        TestDataConfig.getSberbankInn()
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
                                TestDataConfig
                                        .getSberbankInn()
                                        .equals(
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
                        TestDataConfig.getAddressQuery()
                );

        Response response =
                dadataService.suggestAddressWithToken(
                        requestBody,
                        TestDataConfig.getInvalidToken()
                );

        assertEquals(
                TestDataConfig
                        .getInvalidTokenStatus(),
                response.statusCode(),
                "Для невалидного token ожидается заданный статус"
        );
    }
    @Test
    @DisplayName("POST: запрос без token возвращает 401")
    void shouldRejectRequestWithoutToken() {
        AddressSuggestionRequest requestBody =
                new AddressSuggestionRequest(
                        TestDataConfig.getAddressQuery()
                );

        Response response =
                dadataService.suggestAddressWithoutToken(
                        requestBody
                );

        assertEquals(
                TestDataConfig
                        .getMissingTokenStatus(),
                response.statusCode(),
                "Для запроса без token ожидается заданный статус"
        );
    }

    @DisplayName("POST: некорректный query возвращает пустой список подсказок")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("queriesWithoutSuggestions")
    void shouldReturnEmptySuggestionsForInvalidQuery(
            String scenarioName,
            String query
    ) {
        AddressSuggestionRequest requestBody =
                new AddressSuggestionRequest(
                        query
                );

        AddressSuggestionResponse response =
                dadataService.suggestAddress(
                        requestBody
                );

        assertNotNull(
                response,
                "Ответ не должен быть null. Сценарий: "
                        + scenarioName
        );

        assertNotNull(
                response.getSuggestions(),
                "Поле suggestions не должно быть null. Сценарий: "
                        + scenarioName
        );

        assertTrue(
                response.getSuggestions().isEmpty(),
                "Список suggestions должен быть пустым. Сценарий: "
                        + scenarioName
        );
    }
    static Stream<Arguments> queriesWithoutSuggestions() {
        return Stream.of(
                Arguments.of(
                        "пустой query",
                        TestDataConfig.getEmptyQuery()
                ),
                Arguments.of(
                        "короткий query без совпадений",
                        TestDataConfig.getShortQuery()
                )
        );
    }

    @Test
    @DisplayName("GET: IP без результата возвращает location = null")
    void shouldReturnNullLocationForUnknownIp() {
        IpLocateResponse response =
                dadataService.detectAddressByIp(
                        TestDataConfig.getIpWithoutResult()
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