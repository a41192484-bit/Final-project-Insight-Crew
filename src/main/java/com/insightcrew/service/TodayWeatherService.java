package com.insightcrew.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.insightcrew.api.TripApiWeatherClient;
import com.insightcrew.domain.weather.dto.TodayWeatherDto;
import com.insightcrew.domain.weather.dto.WeatherItemDto;
import com.insightcrew.domain.weather.dto.WeeklyWeatherDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodayWeatherService {

    private final TripApiWeatherClient weatherClient;

    /* =====================================================
     * 1️⃣ 오늘 날씨
     * ===================================================== */
    public TodayWeatherDto getTodayWeather(int nx, int ny) {

        List<WeatherItemDto> items = weatherClient.getWeather(nx, ny);
        TodayWeatherDto dto = new TodayWeatherDto();

        for (WeatherItemDto item : items) {
            if (item.getValue() == null) continue;

            switch (item.getCategory()) {
                case "TMP", "T1H" ->
                        dto.setTemp((int) Double.parseDouble(item.getValue()));
                case "WSD" ->
                        dto.setWind(Double.parseDouble(item.getValue()));
                case "POP" ->
                        dto.setRain(Integer.parseInt(item.getValue()));
                case "SKY" ->
                        dto.setSky(convertSky(item.getValue()));
            }
        }

        return dto;
    }

    /* =====================================================
     * 2️⃣ 단기예보 (D+1 ~ D+3)
     * ===================================================== */
    public List<WeeklyWeatherDto> getShortTerm(int nx, int ny) {

        List<WeatherItemDto> items = weatherClient.getWeather(nx, ny);
        List<WeeklyWeatherDto> result = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            WeeklyWeatherDto dto =
                    buildDailyWeather(items, LocalDate.now().plusDays(i));
            if (dto != null) result.add(dto);
        }

        return result;
    }

    /* =====================================================
     * 3️⃣ 하루 단위 요약
     *  - TMX/TMN 우선
     *  - 없으면 TMP 기반 fallback
     *  - SKY는 최신값 사용
     * ===================================================== */
    private WeeklyWeatherDto buildDailyWeather(
            List<WeatherItemDto> items,
            LocalDate targetDate
    ) {
        int maxTemp = Integer.MIN_VALUE;
        int minTemp = Integer.MAX_VALUE;
        int maxRain = 0;
        String sky = null;

        List<Integer> tmpValues = new ArrayList<>();

        String targetDateStr =
                targetDate.format(DateTimeFormatter.BASIC_ISO_DATE);

        for (WeatherItemDto item : items) {

            if (!targetDateStr.equals(item.getFcstDate())) continue;
            if (item.getValue() == null) continue;

            switch (item.getCategory()) {

                case "TMX" ->
                        maxTemp = Math.max(
                                maxTemp,
                                (int) Double.parseDouble(item.getValue())
                        );

                case "TMN" ->
                        minTemp = Math.min(
                                minTemp,
                                (int) Double.parseDouble(item.getValue())
                        );

                case "TMP" ->
                        tmpValues.add(
                                (int) Double.parseDouble(item.getValue())
                        );

                case "POP" ->
                        maxRain = Math.max(
                                maxRain,
                                Integer.parseInt(item.getValue())
                        );

                case "SKY" ->
                        sky = convertSky(item.getValue());
            }
        }

        if (maxTemp == Integer.MIN_VALUE && !tmpValues.isEmpty()) {
            maxTemp = tmpValues.stream().max(Integer::compareTo).orElse(maxTemp);
        }
        if (minTemp == Integer.MAX_VALUE && !tmpValues.isEmpty()) {
            minTemp = tmpValues.stream().min(Integer::compareTo).orElse(minTemp);
        }

        if (maxTemp == Integer.MIN_VALUE || minTemp == Integer.MAX_VALUE) {
            return null;
        }

        WeeklyWeatherDto dto = new WeeklyWeatherDto();
        dto.setDate(targetDate);
        dto.setTempHigh(maxTemp);
        dto.setTempLow(minTemp);
        dto.setRain(maxRain);
        dto.setSky(sky != null ? sky : "맑음");

        return dto;
    }

    /* =====================================================
     * 4️⃣ SKY 코드 변환
     * ===================================================== */
    private String convertSky(String skyCode) {
        return switch (skyCode) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "알수없음";
        };
    }
}
