FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache git

WORKDIR /app

COPY /WeatherAPI-0.0.1.jar /app

CMD ["java", "-jar", "WeatherAPI-0.0.1.jar"]