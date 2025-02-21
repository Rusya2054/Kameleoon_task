package com.Rusya2054.WeatherAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLEngineResult;
import java.util.Map;

/**
 * Service for getting information from <city-coord-service>.
 * Sending data service is made on fastAPI
 * @Author Rusya2054
 */
@Service
public class CityCoordsDataService {

    private final RestTemplate restTemplate;

    @Autowired
    @Value("${city.coords.info.url}")
    private String cityCoordsInfoUrl;

    @Autowired
    public CityCoordsDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @PostConstruct
    private void checkCityCoordsServiceIsWorking(){
        ResponseEntity<Map<String, Object>> entity = getCityCoords("Moscow");
        if (!entity.getStatusCode().equals(HttpStatus.OK)){
             throw new IllegalStateException("Project build error, service <city-coords-service> is not working.");
        }
    }

    public ResponseEntity<Map<String, Object>> getCityCoords(String cityName) {
        ObjectMapper objectMapper = new ObjectMapper();
         try {
            Map<String, Object> result = objectMapper.readValue(
                    restTemplate.getForObject(cityCoordsInfoUrl +"/geocode/"+cityName, String.class),
                    Map.class);
            return ResponseEntity.ok(result);
        } catch (HttpClientErrorException e){
             String errorMessage = e.getResponseBodyAsString();
             Map<String, String> errorMap;
             try {
                errorMap = objectMapper.readValue(errorMessage, Map.class);
                Map<String, Object> errorResponse = Map.of("error", errorMap.getOrDefault("detail", "Unknown error"));
                return ResponseEntity.badRequest().body(errorResponse);
             } catch (JsonProcessingException je){
                 return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of error data"));
             }
         } catch (Exception exception) {
             return ResponseEntity.status(500).body(Map.of("error", "Unknown JSON data format of received data"));
         }

    }

}
