package ru.example.dadata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpLocateResponse {

    private AddressResult location;

    public IpLocateResponse() {
    }

    public AddressResult getLocation() {
        return location;
    }

    public void setLocation(AddressResult location) {
        this.location = location;
    }
}