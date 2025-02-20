package com.Rusya2054.WeatherAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Service for getting information about <code>UserDTO by API. Made by python FastAPI</code>
 * @Author Rusya2054
 */
@Service
public class CityCoordsDataService {

    private final RestTemplate restTemplate;

    @Autowired
    @Value("${city.coord.info.url}")
    private String cityCoordsInfoUrl;


    @Autowired
    public CityCoordsDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<Map<String, Object>> getCityCoords(String cityName) {
        // TODO: долго работает, нужно добавить ассинхронность
        ObjectMapper objectMapper = new ObjectMapper();
         try {
            Map<String, Object> result = objectMapper.readValue(
                    restTemplate.getForObject(cityCoordsInfoUrl+"/geocode/"+cityName, String.class),
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
