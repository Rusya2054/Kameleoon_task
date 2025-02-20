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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service for getting information by using Weather API
 *
 */
@Service
public class WeatherDataService {

    private final RestTemplate restTemplate;
    private final UserDTOService userDTOService;

    @Autowired
    @Value("${weather.api.src.url}")
    private String weatherInfoServiceUrl;


    @Autowired
    public WeatherDataService(RestTemplate restTemplate, UserDTOService userDTOService) {
        this.restTemplate = restTemplate;
        this.userDTOService = userDTOService;
    }

    public Map<String, String> getSrcData(){
        return Map.of();
    }

    public ResponseEntity<Map<String, Object>> getWeatherInformation(WeatherCoords coords, String weatherAPITken) {
        return getWeatherInformation(coords.getLatitude(), coords.getLongitude(), weatherAPITken);
    }

    public ResponseEntity<Map<String, Object>> getWeatherInformation(Double latitude, Double longitude, String weatherAPIToken){
        // TODO: SDK должен обрабатывать любые ошибки, которые могут возникнуть при доступе к weather API, такие как неверный ключ API, проблемы с сетью и другие.

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
                // TODO: лог
                return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of error data"));
            }

         } catch (Exception exception) {
             System.out.println(exception.getMessage());
             return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of received data"));
         }
    }

    public boolean isValidToken(String weatherAPIToken){
        ResponseEntity<Map<String, Object>> response = getWeatherInformation(10.99, 44.34, weatherAPIToken);
        if (response.getStatusCode().equals(HttpStatusCode.valueOf(200))){
            return true;
        }
        return false;
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

    public static Map<String, String> serializeMap(Map<String, Object> originalMap) throws Exception {
        Map<String, String> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            if (entry.getValue() instanceof Map) {
                String serializedValue = objectMapper.writeValueAsString(entry.getValue());
                result.put(entry.getKey(), serializedValue);
            } else {
                result.put(entry.getKey(), entry.getValue().toString());
            }
        }

        return result;
    }
}
