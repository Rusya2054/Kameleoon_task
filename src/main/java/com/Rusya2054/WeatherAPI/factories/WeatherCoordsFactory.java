package com.Rusya2054.WeatherAPI.factories;

import com.Rusya2054.WeatherAPI.models.WeatherCoords;

import java.util.Map;
import java.util.Optional;

public class WeatherCoordsFactory {


    public static WeatherCoords of(String cityName, Double latitude, Double longitude){
        return new WeatherCoords(cityName, latitude,longitude);
    }

    public static Optional<WeatherCoords> coordsOptional(Map<String, Object> map){
        String cityName = (String) map.get("city");

        Double latitude = Double.valueOf((String) map.getOrDefault("latitude", "91.0"));
        Double longitude = Double.valueOf((String) map.getOrDefault("longitude", "181.0"));

        if (cityName == null && latitude.equals(91.0) && longitude.equals(81.0)){
            return Optional.empty();
        }

        return Optional.of(new WeatherCoords(cityName, latitude, longitude));
    }
}
