package ru.example.dadata.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSearchResponse {

    private List<BankSuggestion> suggestions;

    public BankSearchResponse() {
    }

    public List<BankSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(
            List<BankSuggestion> suggestions
    ) {
        this.suggestions = suggestions;
    }
}