package ru.example.dadata.dto;

public class AddressSuggestionRequest {

    private String query;

    public AddressSuggestionRequest() {
    }

    public AddressSuggestionRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}