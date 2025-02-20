package com.Rusya2054.WeatherAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public class WeatherAPIModel {
    @JsonProperty("coord")
    private Coord coord;

    @JsonProperty("weather")
    private List<Weather> weather;

    @JsonProperty("base")
    private String base;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("visibility")
    private int visibility;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("rain")
    private Rain rain;

    @JsonProperty("clouds")
    private Clouds clouds;

    @JsonProperty("dt")
    private long dt;

    @JsonProperty("sys")
    private Sys sys;

    @JsonProperty("timezone")
    private int timezone;

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("cod")
    private int cod;

    private long gettingTime = Instant.now().getEpochSecond();

    public int getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public long getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public String getName() {
        return name;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getGettingTime() {
        return gettingTime;
    }

    public void setGettingTime(long gettingTime) {
        this.gettingTime = gettingTime;
    }

    public static class Coord {
        @JsonProperty("lon")
        private double lon;

        @JsonProperty("lat")
        private double lat;
    }

    public static class Weather {
        @JsonProperty("id")
        private int id;

        @JsonProperty("main")
        private String main;

        @JsonProperty("description")
        private String description;

        @JsonProperty("icon")
        private String icon;

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }
    }

    public Weather getWeather() {
        if (!weather.isEmpty()){
            return weather.get(0);
        }
        return new Weather();
    }

    public static class Main {
        @JsonProperty("temp")
        private double temp;

        @JsonProperty("feels_like")
        private double feels_like;

        @JsonProperty("temp_min")
        private double temp_min;

        @JsonProperty("temp_max")
        private double temp_max;

        @JsonProperty("pressure")
        private int pressure;

        @JsonProperty("humidity")
        private int humidity;

        @JsonProperty("sea_level")
        private int sea_level;

        @JsonProperty("grnd_level")
        private int grnd_level;

        public double getTemp() {
            return temp;
        }

        public double getFeels_like() {
            return feels_like;
        }
    }
    public Main getMain() {
        return main;
    }

    public static class Wind {
        @JsonProperty("speed")
        private double speed;

        @JsonProperty("deg")
        private int deg;
        @JsonProperty("gust")
        private double gust;

        public double getSpeed() {
            return speed;
        }
    }

    public static class Rain {
    }

    public static class Clouds {
        @JsonProperty("all")
        private int all;
    }

    public static class Sys {
        @JsonProperty("type")
        private int type;

        @JsonProperty("id")
        private int id;

        @JsonProperty("country")
        private String country;

        @JsonProperty("sunrise")
        private long sunrise;

        @JsonProperty("sunset")
        private long sunset;

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }
    }
}
