package com.Rusya2054.WeatherAPI.models;


import java.time.LocalDateTime;
import java.time.ZoneId;


public class UserDTO {
    private String ip;
    private String weatherAPIToken;

    private long lastUpdatingTime;

    public void setIp(String ip){
        this.ip = ip;
    }
    public void setLastUpdatingTime(){
        this.lastUpdatingTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
    }

    public String getIp() {
        return ip;
    }

    public void setWeatherAPIToken(String weatherAPIToken) {
        this.weatherAPIToken = weatherAPIToken;
    }

    public String getWeatherAPIToken() {
        return weatherAPIToken;
    }

    public void setLastUpdatingTime(long lastUpdatingTime) {
        this.lastUpdatingTime = lastUpdatingTime;
    }

    @Override
    public String toString(){
        return "UserDTO(ip=%s; weatherAPIToken=%s)".formatted(ip, weatherAPIToken);
    }
}
