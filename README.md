# Kameleoon Weather SDK
Introduction
Develop a SDK for accessing a weather API


Kameleoon React SDK
Introduction
Weather SDK for accessing a weather API.
Weather SDK uses Java Spring Boot and Python FastAPI to work with weather data.
This page describes the basic setup, for a more detailed look at all configuration parameters

## Contents

- [Installation](#installation)
- [Configuration](#configuration)
- [Usage Example](#usage-example)

## Installation
-  `pull git repository` or run `git clone https://github.com/Rusya2054/Kameleoon_task.git`
-  **docker-compose**  run `docker-compose up --build`

## Configuration
- weather-sdk:
   - open WeatherAPI-0.0.1.jar as **ZIP** and go to `WeatherAPI-0.0.1.jar\BOOT-INF\classes\application.properties`
   - go to `src\main\resources\application.properties` for change configurations
   - microservice is listening **8585** port
- city-coords-service
   - service is listening **8282** port
- application
   - if you have changed the settings update `docker-compose.yml`

## Usage Example

This SDK supports two GET requests:

### 1. Initialize `weatherAPIToken`
To initialize the `weatherAPIToken`, make a GET request to:  
`localhost:8585/data`

#### Headers:
```json
{
  "weatherAPIToken": "your_token_here",
  "mode": "polling/waiting"
}
```

 - `weatherAPIToken`: (Required) Your API token for weather data.
 - `mode`: (Optional) Specifies the mode of operation. Default is `waiting`.
    - `polling`: In this mode, the SDK actively polls for new weather information for all saved locations, ensuring zero-latency responses to client requests.
    - `waiting`: In this mode, the SDK updates weather information only upon client requests. This is the default mode if not specified.

#### Example Response::
```json
{
    "success": "Token is initialised",
    "mode": "Waiting Mode"
}
```
- `success`: Confirms that the token has been successfully initialized.
- `mode`: : Indicates the current mode of operation (`Waiting Mode` or `Polling Mode`).


### 2. Fetch Weather Data by City
To retrieve weather data for a specific city, make a GET request to:
`localhost:8585/data/{cityName}`

#### Headers:
```json
{
  "weatherAPIToken": "your_token_here"
}
```
 - `weatherAPIToken`: (Optional) If not provided, the previously initialized token will be used.

#### Example:
To get weather data for `Astana`, use:
`localhost:8585/data/Astana`

#### Example Response::
```json
{"visibility": 10000,
  "weather": {
    "main": "Clear",
    "description": "clear sky"
  },
  "temperature": {
    "feels_like": 260.11, "temp": 265.21
  },
  "wind": {
    "speed":3.0
  }, "timezone": 18000,
  "datetime": 1740129066,
  "name": "Nur-Sultan",
  "sys": {
    "sunrise": 1740104164,
    "sunset": 1740141605}
}
```
- `visibility`: Visibility in meters (e.g., `10000`).  
- `weather`: Weather conditions.  
  - `main`: Main weather condition (e.g., `Clear`).  
  - `description`: Detailed description of the weather (e.g., `clear sky`).  
- `temperature`: Temperature details.  
  - `feels_like`: Feels-like temperature in Kelvin (e.g., `260.11`).  
  - `temp`: Actual temperature in Kelvin (e.g., `265.21`).  
- `wind`: Wind details.  
  - `speed`: Wind speed in meters per second (e.g., `3.0`).  
- `timezone`: Timezone offset in seconds from UTC (e.g., `18000`).  
- `datetime`: Current timestamp in Unix format (e.g., `1740129066`).  
- `name`: City name (e.g., `Nur-Sultan`).  
- `sys`: System data.  
  - `sunrise`: Sunrise time in Unix format (e.g., `1740104164`).  
  - `sunset`: Sunset time in Unix format (e.g., `1740141605`).  

#### Notes:
 - Ensure the weatherAPIToken is valid and properly initialized before making requests for city data.

 - If no token is provided in the headers for the second request, the API will use the last initialized token.