package ru.example.dadata.model.request;

public class IdentifierRequest {

    private String query;

    public IdentifierRequest() {
    }

    public IdentifierRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}