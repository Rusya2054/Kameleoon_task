package com.Rusya2054.WeatherAPI.models;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@ToString
public class UserDTO {
    private String ip;
    private String country;
    private String weatherAPIToken;

    private long lastUpdatingTime;

    private Double latitude;
    private Double longitude;

    public void setIp(String ip){
        this.ip = ip;
    }
    public void setLastUpdatingTime(){
        this.lastUpdatingTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getIp() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public void setWeatherAPIToken(String weatherAPIToken) {
        this.weatherAPIToken = weatherAPIToken;
    }

    @Override
    public String toString(){
        return "UserDTO(ip=%s; latitude=%.2f; longitude=%.2f; weatherAPIToken=%s)".formatted(ip, latitude, longitude, weatherAPIToken);
    }
}
