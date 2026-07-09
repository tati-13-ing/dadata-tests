package ru.example.dadata.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartySearchResponse {

    private List<PartySuggestion> suggestions;

    public PartySearchResponse() {
    }

    public List<PartySuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(
            List<PartySuggestion> suggestions
    ) {
        this.suggestions = suggestions;
    }
}