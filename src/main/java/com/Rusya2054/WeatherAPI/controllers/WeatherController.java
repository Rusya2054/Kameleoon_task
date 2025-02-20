package com.Rusya2054.WeatherAPI.controllers;

import com.Rusya2054.WeatherAPI.factories.WeatherCoordsFactory;
import com.Rusya2054.WeatherAPI.models.UserDTO;
import com.Rusya2054.WeatherAPI.models.WeatherCoords;
import com.Rusya2054.WeatherAPI.models.enums.WeatherAPIWorkMode;
import com.Rusya2054.WeatherAPI.services.CityCoordsDataService;
import com.Rusya2054.WeatherAPI.services.UserDTOService;
import com.Rusya2054.WeatherAPI.services.WeatherDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, String>> getInitWeatherData(HttpServletRequest request){
        Map<String, WeatherAPIWorkMode> modeVariants = Map.of("waiting", WeatherAPIWorkMode.WAITING, "polling", WeatherAPIWorkMode.POLLING);
        String weatherAPIToken = request.getHeader("weatherAPIToken");
        if (weatherAPIToken == null || weatherAPIToken.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error", "The <weatherAPIToken> token is missing or empty in the header. You can obtain your <weatherAPIToken> at https://openweathermap.org/api"));
        }
        String clientIp = request.getRemoteAddr();
        WeatherAPIWorkMode mode = modeVariants.getOrDefault(Objects.requireNonNullElse((String) request.getHeader("mode"), "waiting"), WeatherAPIWorkMode.WAITING);
        UserDTO userDTO = userDTOService.getUserDTO(clientIp, mode);
        if (!weatherDataService.isValidToken(weatherAPIToken)){
            return ResponseEntity.badRequest().body(Map.of("error", "The <weatherAPIToken> is invalid. Please update your token at https://openweathermap.org/api."));
        }
        userDTO.setWeatherAPIToken(weatherAPIToken);
        return ResponseEntity.ok(Map.of("success", "token is initialised"));
    }

    @GetMapping("/{cityName}")
    public ResponseEntity<Map<String, Object>> getCityWeatherData(@PathVariable String cityName, HttpServletRequest request){
         String clientIp = request.getRemoteAddr();
         ResponseEntity<Map<String, Object>> response = cityCoordsDataService.getCityCoords(cityName);
         if (response.getStatusCode().equals(HttpStatusCode.valueOf(400))){
             return ResponseEntity.badRequest().body(response.getBody());
         }
         if (response.getStatusCode().equals(HttpStatusCode.valueOf(500))){
             log.error("error", "Error accessing the service to get the coordinates of the city. Check if the <city-coords-service> service is running");
             return ResponseEntity.badRequest().body(Map.of("error", "Error accessing the service to get the coordinates of the city. Check if the <city-coords-service> service is running"));
         }
         UserDTO userDTO = userDTOService.getUserDTO(clientIp);
         Optional<WeatherCoords> optional = WeatherCoordsFactory.coordsOptional(response.getBody());
         if (optional.isEmpty()){
             return ResponseEntity.badRequest().body(Map.of("error", "Error with the format of the returned data from the <city-coords-service> service"));
         }
         WeatherCoords weatherCoords = optional.get();

         if (userDTO.getWorkMode().equals(WeatherAPIWorkMode.POLLING)){
              return weatherDataService.getPollingWeatherInformation(weatherCoords, userDTO.getWeatherAPIToken());
         }
         if (userDTO.getWeatherAPIToken() == null){
             return weatherDataService.getWaitingWeatherInformation(weatherCoords, request.getHeader("weatherAPIToken"));
         }
        return weatherDataService.getWaitingWeatherInformation(weatherCoords, userDTO.getWeatherAPIToken());
    }


}
