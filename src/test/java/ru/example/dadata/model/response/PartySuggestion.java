package ru.example.dadata.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartySuggestion {

    private PartyData data;

    public PartySuggestion() {
    }

    public PartyData getData() {
        return data;
    }

    public void setData(PartyData data) {
        this.data = data;
    }
}