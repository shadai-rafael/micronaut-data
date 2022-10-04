package io.micronaut.data.azure.entities;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Address {

    private String state;

    private String county;

    private String city;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}