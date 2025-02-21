package com.Rusya2054.WeatherAPI;

import com.Rusya2054.WeatherAPI.services.CityCoordsDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;


@SpringBootTest
public class CityCoordsDataServiceTest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CityCoordsDataService service;

    @Test
    public void connectionTest(){
        ResponseEntity<Map<String, Object>> entity = service.getCityCoords("Astana");
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        entity = service.getCityCoords("Moscow");
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        entity = service.getCityCoords("astana");
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        entity = service.getCityCoords("Tyumen");
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        entity = service.getCityCoords("tyumen");
        assertEquals(entity.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void emptyCityTest(){
        ResponseEntity<Map<String, Object>> entity = service.getCityCoords("");
        assertEquals(entity.getStatusCode(), HttpStatus.valueOf(400));
        System.out.println("emptyCityTest body: %s".formatted(entity.getBody()));

    }

    @Test
    public void unknownCityTest(){
        ResponseEntity<Map<String, Object>> entity = service.getCityCoords("kjkszpj");
        assertEquals(entity.getStatusCode(), HttpStatus.valueOf(400));
        System.out.println("unknownCityTest body: %s".formatted(entity.getBody()));
    }
}
