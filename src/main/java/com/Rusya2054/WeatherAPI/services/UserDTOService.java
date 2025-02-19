package com.Rusya2054.WeatherAPI.services;

import com.Rusya2054.WeatherAPI.models.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class UserDTOService {
    private final ConcurrentHashMap<String, UserDTO> userDTOMap = new ConcurrentHashMap<>(1000);


    public UserDTO getUserDTO(String clientIp){
        if (userDTOMap.containsKey(clientIp)){
            return userDTOMap.get(clientIp);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setIp(clientIp);
        userDTO.setLastUpdatingTime();
        userDTOMap.put(clientIp, userDTO);
        return userDTO;
    }
    public boolean verifyUserDTO(UserDTO userDTO){
        if (userDTO != null && userDTO.getIp() != null && !userDTO.getIp().isEmpty() &&
                userDTO.getCountry() != null && !userDTO.getCountry().isEmpty() &&
                userDTO.getLatitude() != null && userDTO.getLongitude() != null){
            return true;
        }
        return false;
    }
}
