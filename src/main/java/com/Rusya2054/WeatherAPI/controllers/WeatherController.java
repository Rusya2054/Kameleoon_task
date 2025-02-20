package com.Rusya2054.WeatherAPI.controllers;

import com.Rusya2054.WeatherAPI.factories.WeatherCoordsFactory;
import com.Rusya2054.WeatherAPI.models.UserDTO;
import com.Rusya2054.WeatherAPI.models.WeatherCoords;
import com.Rusya2054.WeatherAPI.services.CityCoordsDataService;
import com.Rusya2054.WeatherAPI.services.UserDTOService;
import com.Rusya2054.WeatherAPI.services.WeatherDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("data")
public class WeatherController {
    private final UserDTOService userDTOService;
    private final WeatherDataService weatherDataService;
    private final CityCoordsDataService cityCoordsDataService;


    public WeatherController(UserDTOService userDTOService,
                             WeatherDataService weatherDataService,
                             CityCoordsDataService cityCoordsDataService){
        this.userDTOService = userDTOService;
        this.weatherDataService = weatherDataService;
        this.cityCoordsDataService = cityCoordsDataService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, String>> getSimpleWeatherData(HttpServletRequest request){
        // TODO: кешировать и В БД Map<Spring, ...>
        String weatherAPIToken = request.getHeader("weatherAPIToken");
        if (weatherAPIToken == null || weatherAPIToken.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error", "The <weatherAPIToken> token is missing or empty in the header. You can obtain your <weatherAPIToken> at https://openweathermap.org/api"));
        }
        String clientIp = request.getRemoteAddr();

        UserDTO userDTO = userDTOService.getUserDTO(clientIp);
        if (!weatherDataService.isValidToken(weatherAPIToken)){
            // TODO: скорость нужно увеличить как вариант добавить ассинхронность чтобы другие запросы потоки обрабатывали
            return ResponseEntity.badRequest().body(Map.of("error", "The <weatherAPIToken> is invalid. Please update your token at https://openweathermap.org/api."));
        }
        userDTO.setWeatherAPIToken(weatherAPIToken);
        return ResponseEntity.ok(Map.of("success", "token is initialised"));
    }

    @GetMapping("/{cityName}")
    public ResponseEntity<Map<String, Object>> getCityWeatherData(@PathVariable String cityName, HttpServletRequest request){

        // TODO: Проверить если не будет запущем сервис
//        // TODO: кешировать и В БД Map<Spring, ...>

//           if (weatherAPIToken == null || weatherAPIToken.isEmpty()){
//            return ResponseEntity.badRequest().body(Map.of("error", "Missing or empty \"weatherAPIToken\" token in header"));
//        }
         String clientIp = request.getRemoteAddr();
         ResponseEntity<Map<String, Object>> response = cityCoordsDataService.getCityCoords(cityName);
         if (response.getStatusCode().equals(HttpStatusCode.valueOf(400))){
             return ResponseEntity.badRequest().body(response.getBody());
         }

         if (response.getStatusCode().equals(HttpStatusCode.valueOf(500))){
             return ResponseEntity.badRequest().body(Map.of("error", "we're in the process of fixing the mistake"));
         }
         UserDTO userDTO = userDTOService.getUserDTO(clientIp);
         Optional<WeatherCoords> optional = WeatherCoordsFactory.coordsOptional(response.getBody());
         if (optional.isEmpty()){
             return ResponseEntity.badRequest().body(Map.of("error", "we're in the process of fixing the mistake"));
         }
         String weatherAPIToken = (userDTO.getWeatherAPIToken() != null)? userDTO.getWeatherAPIToken() : request.getHeader("weatherAPIToken");
         WeatherCoords weatherCoords = optional.get();

        return weatherDataService.getWeatherInformation(weatherCoords, weatherAPIToken);
    }


}
