package com.Rusya2054.WeatherAPI.models;


import com.Rusya2054.WeatherAPI.models.enums.WeatherAPIWorkMode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class UserDTO {
    private String weatherAPIToken;
    private final String ip;

    private WeatherAPIWorkMode workMode = WeatherAPIWorkMode.WAITING;

    public UserDTO(String ip){
        this.ip = ip;
    }

    public void setWeatherAPIToken(String weatherAPIToken) {
        this.weatherAPIToken = weatherAPIToken;
    }

    public String getWeatherAPIToken() {
        return weatherAPIToken;
    }

    @Override
    public String toString(){
        return "UserDTO(ip=%s; weatherAPIToken=%s)".formatted(ip, weatherAPIToken);
    }

    public WeatherAPIWorkMode getWorkMode() {
        return workMode;
    }

    public String getIp() {
        return ip;
    }

    public void setWorkMode(WeatherAPIWorkMode workMode) {
        this.workMode = workMode;
    }


    @Override
    public int hashCode(){
        return this.weatherAPIToken.hashCode() + 31;
    }
}
