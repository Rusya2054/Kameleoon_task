package com.Rusya2054.WeatherAPI.controllers;

import com.Rusya2054.WeatherAPI.models.UserDTO;
import com.Rusya2054.WeatherAPI.services.ClientIpDataService;
import com.Rusya2054.WeatherAPI.services.UserDTOService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("data")
public class WeatherController {
    private final UserDTOService userDTOService;
    private final ClientIpDataService clientIpDataService;
    public WeatherController(UserDTOService userDTOService, ClientIpDataService clientIpDataService){
        this.userDTOService = userDTOService;
        this.clientIpDataService = clientIpDataService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, String>> getSimpleWeatherData(HttpServletRequest request){
        // TODO: кешировать и В БД Map<Spring, ...>
        String weatherAPIToken = request.getHeader("Bearer");
        if (weatherAPIToken == null || weatherAPIToken.isEmpty()){
             Map<String, String> errorResponse = Map.of("error", "Missing or empty Bearer token");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        String clientIp = request.getRemoteAddr();
        UserDTO userDTO = userDTOService.getUserDTO(clientIp);
        System.out.println(userDTO);
        if (!userDTOService.verifyUserDTO(userDTO)){
            System.out.println(clientIpDataService.getIpInfo(clientIp));
            // TODO: получение информации и добавлениие
        }
        // TODO: ассинхронное выполнение на другой запрос и получение ширины и долготы

//        String userAgent = request.getHeader("User-Agent");
//
//
//        System.out.println(clientIp);
//        System.out.println(userAgent);
        // TODO: сервис получения долготы и широты через python
        return ResponseEntity.ok(Map.of());
    }

}
