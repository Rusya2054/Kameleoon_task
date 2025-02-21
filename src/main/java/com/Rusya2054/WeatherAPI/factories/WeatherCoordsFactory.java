package com.Rusya2054.WeatherAPI.factories;

import com.Rusya2054.WeatherAPI.models.WeatherCoords;

import java.util.Map;
import java.util.Optional;


/**
 * Factory class for creating instances of WeatherCoords.
 * Provides two methods to create WeatherCoords objects: one directly and one optionally based on a map of values.
 * @author Rusya2054
 */
public class WeatherCoordsFactory {


    /**
     * Creates a new WeatherCoords object with the specified city name, latitude, and longitude.
     *
     * @param cityName the name of the city.
     * @param latitude the latitude of the city.
     * @param longitude the longitude of the city.
     * @return a new instance of WeatherCoords.
     */
    public static WeatherCoords of(String cityName, Double latitude, Double longitude){
        return new WeatherCoords(cityName, latitude,longitude);
    }


    /**
     * Creates an optional WeatherCoords object based on the data provided in the map.
     * <p>
     * The map should contain the following keys:
     * - "city" (String): the name of the city.
     * - "latitude" (String): the latitude, which will be converted to Double. Defaults to "91.0" if absent.
     * - "longitude" (String): the longitude, which will be converted to Double. Defaults to "181.0" if absent.
     * </p>
     * If the map contains invalid or missing data (i.e., cityName is null and latitude/longitude are default values),
     * this method returns Optional.empty().
     *
     * @param map the map containing the data for city, latitude, and longitude.
     * @return an Optional containing a WeatherCoords object if valid data is found; Optional.empty() otherwise.
     */
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
