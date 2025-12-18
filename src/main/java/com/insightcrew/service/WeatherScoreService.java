package com.insightcrew.service;

import org.springframework.stereotype.Service;

import com.insightcrew.domain.weather.vo.WeatherForecastVo;

@Service
public class WeatherScoreService {

    /**
     * WeatherForecastVo (오늘 데이터) 기반 여행 적합도 점수 계산
     */
    public int calculateScore(WeatherForecastVo w) {

        if (w == null) return 0;

        int score = 0;

        // =====================================================
        // 🌧 강수 확률 (가중치 가장 큼)
        // =====================================================
        if (w.getRainPercent() != null) {
            int rain = w.getRainPercent();

            if (rain < 20) {
                score += 40;          // 비 거의 없음
            } else if (rain < 40) {
                score += 20;          // 약간 가능성
            } else if (rain < 60) {
                score -= 10;          // 애매
            } else {
                score -= 50;          // 비 확률 높음 → 여행 비추천
            }
        }

        // =====================================================
        // ☁ 구름량
        // =====================================================
        if (w.getCloudPercent() != null) {
            int cloud = w.getCloudPercent();

            if (cloud < 20) {
                score += 30;          // 쾌청
            } else if (cloud < 50) {
                score += 10;          // 적당
            } else if (cloud < 80) {
                score -= 5;           // 흐림
            } else {
                score -= 15;          // 매우 흐림
            }
        }

        // =====================================================
        // 🌡 기온 (최고/최저 평균)
        // =====================================================
        if (w.getMinTemp() != null && w.getMaxTemp() != null) {
            int avgTemp = (w.getMinTemp() + w.getMaxTemp()) / 2;

            if (avgTemp >= 15 && avgTemp <= 23) {
                score += 25;          // 여행 최적
            } else if (avgTemp >= 5 && avgTemp <= 30) {
                score += 10;          // 무난
            } else {
                score -= 15;          // 너무 춥거나 더움
            }
        }

        // =====================================================
        // 🍃 바람
        // =====================================================
        if (w.getWindSpeed() != null) {
            int wind = w.getWindSpeed();

            if (wind < 4) {
                score += 10;          // 쾌적
            } else if (wind < 8) {
                score += 0;           // 보통
            } else {
                score -= 10;          // 강풍
            }
        }

        return score;
    }
}
