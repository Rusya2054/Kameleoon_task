package com.Rusya2054.WeatherAPI.services;

import com.Rusya2054.WeatherAPI.models.WeatherAPIModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing and updating weather data in a cache, specifically for different city names.
 * <p>
 * The service provides methods to update the weather data for a specific city and retrieve the most recent data.
 * The cache is limited in size, and older data is evicted if the cache exceeds the maximum length or if the data is outdated.
 * </p>
 * @author Rusya2054
 */
@Component
public class WeatherCacheService {
    /**
    * A map storing weather data, keyed by city name.
    * The map is thread-safe and ensures concurrent access is handled.
    */
    private final Map<String, WeatherAPIModel> cityNameWeatherMap = new ConcurrentHashMap<>(16);
    /**
    * Maximum number of entries allowed in the cache before evicting older data.
    * The cache size is now set to 10.
    */
    private final int MAP_MAX_LENGTH = 10;
    /**
    * The time in seconds after which the weather data is considered outdated and may need to be refreshed.
    */
    private final long UPDATING_SECONDS_TIME_VALUE = 10*60;

    /**
     * Updates the weather data for the given city if the existing data is outdated or if the cache size allows for a new entry.
     * If the cache exceeds the maximum size, the service will remove the oldest entry that is outdated.
     *
     * @param cityName The name of the city for which weather data is being updated.
     * @param weatherModel The new weather data to store for the city.
     * @return An {@link Optional} containing the updated weather data, or an empty {@link Optional} if the cache could not be updated.
     */
    public Optional<WeatherAPIModel> updateData(String cityName, WeatherAPIModel weatherModel){
        long currentTime = Instant.now().getEpochSecond();
        if (cityNameWeatherMap.containsKey(cityName)){
            // updating of existing data
            WeatherAPIModel model = cityNameWeatherMap.get(cityName);
            if (currentTime - model.getGettingTime() >= UPDATING_SECONDS_TIME_VALUE){
                cityNameWeatherMap.put(cityName, weatherModel);
                return Optional.of(weatherModel);
            }
        }
        if (cityNameWeatherMap.size() >= MAP_MAX_LENGTH){
             // If the cache is full, remove outdated entries
            Iterator<Map.Entry<String, WeatherAPIModel>> iterator = cityNameWeatherMap.entrySet().iterator();
            boolean toMod = false;
            while (iterator.hasNext()){
                Map.Entry<String, WeatherAPIModel> entry = iterator.next();
                WeatherAPIModel model = entry.getValue();
                if (currentTime - model.getGettingTime() >= UPDATING_SECONDS_TIME_VALUE){
                    iterator.remove();
                    toMod = true;
                    break;
                }
            }
            if (toMod){
                cityNameWeatherMap.put(cityName, weatherModel);
                return Optional.of(weatherModel);
            }
            return Optional.empty();

        } else {
            cityNameWeatherMap.put(cityName, weatherModel);

        }
       return Optional.of(weatherModel);
    }

    /**
     * Retrieves the weather data for the given city, if the data is still fresh.
     * The data is considered fresh if it was updated within the defined expiration time.
     *
     * @param cityName The name of the city for which to retrieve the weather data.
     * @return An {@link Optional} containing the weather data if fresh data exists, or an empty {@link Optional} if no fresh data is available.
     */
    public Optional<WeatherAPIModel> getData(String cityName){
        long currentTime = Instant.now().getEpochSecond();
        if (cityNameWeatherMap.containsKey(cityName)){
            // updating of existing data
            WeatherAPIModel model = cityNameWeatherMap.get(cityName);
            if (currentTime - model.getGettingTime() <= UPDATING_SECONDS_TIME_VALUE){
                return Optional.of(cityNameWeatherMap.get(cityName));
            }
        }
        return Optional.empty();
    }
}
