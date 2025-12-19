package com.insightcrew.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.insightcrew.api.dto.OpenWeatherResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpenWeatherApiClient {

    @Value("${openweather.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    /**
     * OpenWeather One Call API (Daily 포함)
     */
    public OpenWeatherResponse getWeekly(double lat, double lon) {

        String url =
            "https://api.openweathermap.org/data/3.0/onecall"
          + "?lat=" + lat
          + "&lon=" + lon
          + "&exclude=minutely,hourly,alerts"
          + "&units=metric"
          + "&lang=kr"
          + "&appid=" + apiKey;

        return restTemplate.getForObject(url, OpenWeatherResponse.class);
    }
}
