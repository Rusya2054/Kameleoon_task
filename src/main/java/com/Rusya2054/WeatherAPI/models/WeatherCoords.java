package com.Rusya2054.WeatherAPI.models;

public class WeatherCoords {

    private final String cityName;
    private final Double latitude;
    private final Double longitude;

    public WeatherCoords(String cityName, Double latitude, Double longitude){
        this.longitude = longitude;
        this.latitude = latitude;
        this.cityName = cityName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
