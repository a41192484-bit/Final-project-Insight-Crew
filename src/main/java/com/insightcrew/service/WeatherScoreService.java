package com.insightcrew.service;

import org.springframework.stereotype.Service;

import com.insightcrew.domain.weather.vo.WeatherTodayVo;

@Service
public class WeatherScoreService {

    /**
     * WeatherTodayVo 기반 점수 계산
     */
    public int calculateScore(WeatherTodayVo w) {

        if (w == null) return 0;

        int score = 0;

        // 🌧 PTY (강수 여부)
        if (w.getPty() != null) {
            int p = w.getPty();
            if (p == 0) {
                score += 40; // 비/눈 없음 → 매우 좋음
            } else {
                score -= 100; // 비/눈 → 여행 치명적
            }
        }

        // ☀ SKY (하늘 상태)
        if (w.getSky() != null) {
            int sky = w.getSky();
            switch (sky) {
                case 1 -> score += 30;   // 맑음
                case 3 -> score += 10;   // 구름 많음
                case 4 -> score -= 10;   // 흐림
            }
        }

        // 🌡 기온 (T1H)
        if (w.getTemp() != null) {
            double t = w.getTemp();

            if (t >= 15 && t <= 23) {
                score += 20;        // 최적 기온
            } else if (t >= 5 && t <= 30) {
                score += 10;        // 적당함
            }
        }

        // 🍃 바람 (풍속)
        if (w.getWind() != null) {
            double wind = w.getWind();

            if (wind < 4) {
                score += 10;        // 바람 약함 → 좋음
            }
        }

        return score;
    }
}
