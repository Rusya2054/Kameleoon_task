package com.Rusya2054.WeatherAPI.models;


import com.Rusya2054.WeatherAPI.models.enums.WeatherAPIWorkMode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class represents a data transfer object (DTO) used for managing user-related data, specifically regarding the Weather API token and work mode.
 * @author Rusya2054
 */
@Data
@Getter
@ToString
@Setter
public class UserDTO {
    /**
     * The token used to initialize work with the Weather API.
     * */
    private String weatherAPIToken;
    /**
     * The IP address associated with the user.
     * This field is final and must be provided during object instantiation.
     */
    private final String ip;

    /**
     * The operational mode for interacting with the Weather API.
     * Defaults to {@link WeatherAPIWorkMode#WAITING}.
     */
    private WeatherAPIWorkMode workMode = WeatherAPIWorkMode.WAITING;
}
