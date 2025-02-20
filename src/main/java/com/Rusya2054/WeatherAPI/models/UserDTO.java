package com.Rusya2054.WeatherAPI.models;


import com.Rusya2054.WeatherAPI.models.enums.WeatherAPIWorkMode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Getter
@ToString
@Setter
public class UserDTO {
    private String weatherAPIToken;
    private final String ip;

    private WeatherAPIWorkMode workMode = WeatherAPIWorkMode.WAITING;
}
