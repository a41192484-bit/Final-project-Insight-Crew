package com.insightcrew.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class OpenWeatherResponse {

    private List<Daily> daily;

    @Data
    public static class Daily {

        private long dt;
        private Temp temp;
        private double pop;

        private double windSpeed;
        private int clouds;

        private List<Weather> weather;
    }

    @Data
    public static class Temp {
        private double min;
        private double max;
    }

    @Data
    public static class Weather {
        private String description;
        private String icon;
    }
}
