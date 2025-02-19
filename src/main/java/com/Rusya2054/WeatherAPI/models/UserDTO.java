package com.Rusya2054.WeatherAPI.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class UserDTO {
    private String ip;
    private String country;
    private String weatherToken;

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
    // TODO: добавить ширину и долготу
}
