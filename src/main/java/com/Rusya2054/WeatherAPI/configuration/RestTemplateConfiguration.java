package com.Rusya2054.WeatherAPI.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for initialize restTemplate bean
 * @author Rusya2054
 */
@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplateInit(){
        return new RestTemplate();
    }
}
