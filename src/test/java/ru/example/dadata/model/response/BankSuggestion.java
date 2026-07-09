package ru.example.dadata.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSuggestion {

    private BankData data;

    public BankSuggestion() {
    }

    public BankData getData() {
        return data;
    }

    public void setData(BankData data) {
        this.data = data;
    }
}