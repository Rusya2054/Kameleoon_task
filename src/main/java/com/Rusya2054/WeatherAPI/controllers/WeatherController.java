package com.Rusya2054.WeatherAPI.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("data")
public class WeatherController {

    @GetMapping("")
    public Map<String, String> getSimpleWeatherData(HttpServletRequest request){
        // TODO: кешировать и В БД Map<Spring, ...>
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        // TODO: сервис получения долготы и широты через python
        return Map.of();
    }

}
