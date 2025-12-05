package com.insightcrew.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.insightcrew.domain.weather.dto.WeatherItemDto;

@Service
public class TripWeatherScoreService {

    public int calculateScore(List<WeatherItemDto> items) {

        int score = 0;

        String pty = getValue(items, "PTY"); // 강수형태
        String sky = getValue(items, "SKY"); // 맑음/흐림
        String t1h = getValue(items, "T1H"); // 기온
        String wsd = getValue(items, "WSD"); // 풍속

        // ❗ PTY (비/눈 여부)
        if ("0".equals(pty)) score += 40; // 강수 없음
        else score -= 100; // 비/눈 → 여행 비적합

        // ☀ SKY 하늘 상태
        if ("1".equals(sky)) score += 30; // 맑음
        if ("3".equals(sky)) score += 10; // 구름많음
        if ("4".equals(sky)) score -= 10; // 흐림

        // 🌡 기온
        if (!t1h.isEmpty()) {
            int temp = Integer.parseInt(t1h);
            if (temp >= 15 && temp <= 23) score += 20;
            else if (temp >= 5 && temp <= 30) score += 10;
        }

        // 🍃 풍속
        if (!wsd.isEmpty()) {
            double wind = Double.parseDouble(wsd);
            if (wind < 4) score += 10;
        }

        return score;
    }

    private String getValue(List<WeatherItemDto> items, String category) {
        return items.stream()
                .filter(i -> category.equals(i.getCategory()))
                .map(WeatherItemDto::getFcstValue)
                .findFirst().orElse("");
    }
}
