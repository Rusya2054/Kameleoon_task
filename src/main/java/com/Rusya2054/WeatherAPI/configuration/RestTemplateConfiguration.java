package com.Rusya2054.WeatherAPI.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for initialize restTemplate bean
 * @author Rusya2054
 */
@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplateInit(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3500);
        return new RestTemplate(factory);
    }
}
