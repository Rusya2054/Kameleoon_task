package com.Rusya2054.WeatherAPI.services;

import com.Rusya2054.WeatherAPI.models.WeatherAPIModel;
import com.Rusya2054.WeatherAPI.models.WeatherCoords;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;


/**
 * Service for interacting with a weather information API, providing both "waiting" and "polling" modes of data retrieval.
 * <p>
 * The service fetches weather data from an external API based on geographic coordinates (latitude and longitude) or city name.
 * The "waiting" mode retrieves fresh data each time, while the "polling" mode checks the cache first before querying the API.
 * </p>
 * @author Rusya2054
 */
@Slf4j
@Service
public class WeatherDataService {

    /**
    * The RestTemplate used to make HTTP requests to the weather API.
    */
    private final RestTemplate restTemplate;
    /**
    * Service used for caching weather data.
    */
    private final WeatherCacheService weatherCacheService;
    /**
    * URL of the weather API, injected from application properties.
    */
    @Autowired
    @Value("${weather.api.src.url}")
    private String weatherInfoServiceUrl;


    @Autowired
    public WeatherDataService(RestTemplate restTemplate, WeatherCacheService weatherCacheService) {
        this.restTemplate = restTemplate;
        this.weatherCacheService = weatherCacheService;
    }

    public ResponseEntity<Map<String, Object>> getWaitingWeatherInformation(WeatherCoords coords, String weatherAPIToken) {
        if (weatherAPIToken == null){
            return ResponseEntity.badRequest().body(Map.of("error", "<weatherAPIToken> is null. Please initialize <weatherAPIToken> or add <weatherAPIToken> to headers of GET request"));
        }
        return getWaitingWeatherInformation(coords.getCityName(), coords.getLatitude(), coords.getLongitude(), weatherAPIToken);
    }
    public ResponseEntity<Map<String, Object>> getPollingWeatherInformation(WeatherCoords coords, String weatherAPIToken) {
        if (weatherAPIToken == null){
            return ResponseEntity.badRequest().body(Map.of("error", "<weatherAPIToken> is null. Please initialize <weatherAPIToken> or add <weatherAPIToken> to headers of GET request"));
        }
        return getPollingWeatherInformation(coords.getCityName(), coords.getLatitude(), coords.getLongitude(), weatherAPIToken);
    }

    /**
    * Retrieves weather information in "waiting" {@link com.Rusya2054.WeatherAPI.models.enums.WeatherAPIWorkMode}  mode (always fetches fresh data).
    *
    * @param cityName The name of the city.
    * @param latitude The latitude of the city.
    * @param longitude The longitude of the city.
    * @param weatherAPIToken The API token for authenticating with the weather service.
    * @return A {@link ResponseEntity} containing the weather data or an error message.
    */
    public ResponseEntity<Map<String, Object>> getWaitingWeatherInformation(String cityName, Double latitude, Double longitude, String weatherAPIToken){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
             String url = String.format("%s/weather?lat=%.2f&lon=%.2f&appid=%s",
                           weatherInfoServiceUrl, latitude, longitude, weatherAPIToken);

            WeatherAPIModel result = objectMapper.readValue(
                    restTemplate.getForObject(url, String.class),
                    WeatherAPIModel.class);
            return ResponseEntity.ok(getWeatherResponse(result));
        } catch (HttpClientErrorException e){
            try {
                Map<String, String> errorMessage = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
                return ResponseEntity.badRequest().body(Map.of("error", errorMessage.get("message")));
            } catch (JsonProcessingException jpe){
                log.error("Unknown JSON data format of error data in getWaitingWeatherInformation");
                return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of error data"));
            }

         } catch (Exception exception) {
             System.out.println(exception.getMessage());
             return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of received data"));
         }
    }

    /**
    * Retrieves weather information in "polling"{@link com.Rusya2054.WeatherAPI.models.enums.WeatherAPIWorkMode} mode (checks cache first, fetches from API if not cached).
    *
    * @param cityName The name of the city.
    * @param latitude The latitude of the city.
    * @param longitude The longitude of the city.
    * @param weatherAPIToken The API token for authenticating with the weather service.
    * @return A {@link ResponseEntity} containing the weather data or an error message.
    */
    public ResponseEntity<Map<String, Object>> getPollingWeatherInformation(String cityName, Double latitude, Double longitude, String weatherAPIToken){
        Optional<WeatherAPIModel> model = weatherCacheService.getData(cityName);
        if (model.isPresent()){
            return ResponseEntity.ok(getWeatherResponse(model.get()));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
             String url = String.format("%s/weather?lat=%.2f&lon=%.2f&appid=%s",
                           weatherInfoServiceUrl, latitude, longitude, weatherAPIToken);

            WeatherAPIModel result = objectMapper.readValue(
                    restTemplate.getForObject(url, String.class),
                    WeatherAPIModel.class);
            weatherCacheService.updateData(cityName, result);
            return ResponseEntity.ok(getWeatherResponse(result));
        } catch (HttpClientErrorException e){
            try {
                Map<String, String> errorMessage = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
                return ResponseEntity.badRequest().body(Map.of("error", errorMessage.get("message")));
            } catch (JsonProcessingException jpe){
                log.error("Unknown JSON data format of error data in getPollingWeatherInformation");
                return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of error data"));
            }

         } catch (Exception exception) {
             System.out.println(exception.getMessage());
             return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of received data"));
         }
    }

     /**
     * Verifies if the provided <weatherAPIToken> token is valid by checking the weather data for a default city (Moscow).
     *
     * @param weatherAPIToken The API token to verify.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean isValidToken(String weatherAPIToken){
        ResponseEntity<Map<String, Object>> response = getWaitingWeatherInformation("Moscow", 55.625578,37.6063916 , weatherAPIToken);
        return response.getStatusCode().equals(HttpStatusCode.valueOf(200));
    }
    private Map<String, Object> getWeatherResponse(WeatherAPIModel model) {

        Map<String, Object> response = Map.of(
            "weather", Map.of("main", model.getWeather().getMain(), "description", model.getWeather().getDescription()),
                "temperature", Map.of("temp", model.getMain().getTemp(), "feels_like", model.getMain().getFeels_like()),
                "visibility", model.getVisibility(),
                "wind", Map.of("speed", model.getWind().getSpeed()),
                "datetime", model.getDt(),
                "sys", Map.of("sunrise", model.getSys().getSunrise(), "sunset", model.getSys().getSunset()),
                "timezone", model.getTimezone(),
                "name", model.getName()
        );
        return response;
    }
}
