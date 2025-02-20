package com.Rusya2054.WeatherAPI.services;

import com.Rusya2054.WeatherAPI.models.UserDTO;
import com.Rusya2054.WeatherAPI.models.enums.WeatherAPIWorkMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class UserDTOService {
    private final ConcurrentHashMap<String, UserDTO> userDTOMap = new ConcurrentHashMap<>(1000);

    public UserDTO getUserDTO(String clientIp, WeatherAPIWorkMode mode){
        if (userDTOMap.containsKey(clientIp)){
            UserDTO  userDTO = userDTOMap.get(clientIp);
            userDTO.setWorkMode(mode);
            return userDTOMap.get(clientIp);
        }
        UserDTO userDTO = new UserDTO(clientIp);
        userDTO.setWorkMode(mode);
        userDTOMap.put(clientIp, userDTO);
        return userDTO;
    }

    public UserDTO getUserDTO(String clientIp){
        if (userDTOMap.containsKey(clientIp)){
            return userDTOMap.get(clientIp);
        }
        UserDTO userDTO = new UserDTO(clientIp);
        userDTOMap.put(clientIp, userDTO);
        return userDTO;
    }
    public boolean verifyUserDTO(UserDTO userDTO){
        if (userDTO != null && userDTO.getIp() != null && !userDTO.getIp().isEmpty() && userDTO.getWeatherAPIToken() != null && !userDTO.getWeatherAPIToken().isEmpty()){
            return true;
        }
        return false;
    }
}
