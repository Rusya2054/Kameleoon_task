package com.Rusya2054.WeatherAPI.models.enums;

public enum WeatherAPIWorkMode {
    WAITING("Waiting Mode"),
    POLLING("Polling Mode");

    private final String description;

    WeatherAPIWorkMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
