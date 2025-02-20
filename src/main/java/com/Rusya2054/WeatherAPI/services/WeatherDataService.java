package com.Rusya2054.WeatherAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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


    public ResponseEntity<Map<String, String>> getWeatherInformation(Double latitude, Double longitude, String weatherAPIToken) {
        // TODO: SDK должен обрабатывать любые ошибки, которые могут возникнуть при доступе к weather API, такие как неверный ключ API, проблемы с сетью и другие.

        ObjectMapper objectMapper = new ObjectMapper();
        try {
             String url = String.format("%s/weather?lat=%.2f&lon=%.2f&appid=%s",
                           weatherInfoServiceUrl, latitude, longitude, weatherAPIToken);
            Map<String, String> result = objectMapper.readValue(
                    restTemplate.getForObject(url, String.class),
                    Map.class);
            return ResponseEntity.ok(result);
        } catch (HttpClientErrorException e){
             return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
         } catch (Exception exception) {
             System.out.println(exception.getMessage());
             return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of received data"));
         }


    }

    public boolean isValidToken(String weatherAPIToken){
        ResponseEntity<Map<String, String>> response = getWeatherInformation(10.99, 44.34, weatherAPIToken);
        if (response.getStatusCode().equals(HttpStatusCode.valueOf(200))){
            return true;
        }
        return false;
    }
}
