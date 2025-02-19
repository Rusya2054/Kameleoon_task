package com.Rusya2054.WeatherAPI.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Service for getting information about <code>UserDTO by API. Made by python FastAPI</code>
 * @Author Rusya2054
 */
@Service
public class ClientIpDataService {

    private final RestTemplate restTemplate;

    @Autowired
    @Value("${client.ip.info.url}")
    private String clientIpInfoUrl;


    @Autowired
    public ClientIpDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Map<String, String>> getIpInfo(String clientIp){
        ObjectMapper objectMapper = new ObjectMapper();
         try {
            Map<String, String> result = objectMapper.readValue(
                    restTemplate.getForObject(clientIpInfoUrl+"/ip-info/"+clientIp, String.class),
                    Map.class);
            return ResponseEntity.ok(result);
        } catch (Exception e){
             Map<String, String> errorResponse = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
         }

    }

}
