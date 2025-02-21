package com.Rusya2054.WeatherAPI.models.enums;


/**
 * Enum represents the different operational modes for interacting with a weather API.
 * Constants:
 *  - WAITING: Represents the "Waiting Mode" for the Weather API.
 *  - POLLING: Represents the "Polling Mode" for the Weather API.
 */
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
