package com.Rusya2054.WeatherAPI.services;

import com.Rusya2054.WeatherAPI.models.WeatherAPIModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WeatherCacheService {
    private final Map<String, WeatherAPIModel> cityNameWeatherMap = new ConcurrentHashMap<>(16);
    private final int MAP_MAX_LENGTH = 2;
    private final long UPDATING_SECONDS_TIME_VALUE = 10*60;

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
