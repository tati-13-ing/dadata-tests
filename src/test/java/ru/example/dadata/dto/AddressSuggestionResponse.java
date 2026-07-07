package ru.example.dadata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressSuggestionResponse {

    private List<AddressResult> suggestions;

    public AddressSuggestionResponse() {
    }

    public List<AddressResult> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<AddressResult> suggestions) {
        this.suggestions = suggestions;
    }
}