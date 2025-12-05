package com.insightcrew.domain.weather.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WeatherApiResponse {
    private Response response;

    @Getter @Setter
    public static class Response {
        private Body body;
    }

    @Getter @Setter
    public static class Body {
        private Items items;
    }

    @Getter @Setter
    public static class Items {
        private List<WeatherItemDto> item;
    }
}