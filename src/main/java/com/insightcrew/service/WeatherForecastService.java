package com.insightcrew.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;

import com.insightcrew.api.OpenWeatherApiClient;
import com.insightcrew.api.dto.OpenWeatherResponse;
import com.insightcrew.domain.weather.vo.WeatherForecastVo;
import com.insightcrew.repository.WeatherForecastMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherForecastService {

    private final OpenWeatherApiClient openWeatherApiClient;
    private final WeatherForecastMapper weatherForecastMapper;

    /**
     * 메인 화면용 주간 예보 조회 (1~7일)
     * - 캐시 키: regionId
     * - API 파라미터: lat / lon
     */
    public List<WeatherForecastVo> getForecast(Integer regionId) {
        return weatherForecastMapper.findWeeklyByRegion(regionId);
    }

    public WeatherForecastVo getToday(Integer regionId) {
        return weatherForecastMapper.findTodayByRegion(regionId);
    }

    /**
     * 오늘 날씨 단건 조회
     */
    public WeatherForecastVo getToday(Integer regionId, double lat, double lon) {

        boolean cached = weatherForecastMapper.existsRecentCache(
            regionId,
            LocalDateTime.now().minusHours(24)
        );

        if (!cached) {
            updateForecast(regionId, lat, lon);
        }

        return weatherForecastMapper.findTodayByRegion(regionId);
    }

    /**
     * OpenWeather → weather_forecast_cache 저장
     * - 오늘 포함 최대 7일
     * - regionId 기준 upsert
     */
    private void updateForecast(Integer regionId, double lat, double lon) {

        OpenWeatherResponse response =
            openWeatherApiClient.getWeekly(lat, lon);

        if (response == null || response.getDaily() == null || response.getDaily().isEmpty()) {
            throw new IllegalStateException("OpenWeather daily response is empty.");
        }

        int days = Math.min(7, response.getDaily().size());

        for (int i = 0; i < days; i++) {

            OpenWeatherResponse.Daily daily = response.getDaily().get(i);

            WeatherForecastVo vo = new WeatherForecastVo();
            vo.setRegionId(regionId);

            vo.setWeatherDate(
                Instant.ofEpochSecond(daily.getDt())
                       .atZone(ZoneId.of("Asia/Seoul"))
                       .toLocalDate()
            );

            vo.setMinTemp((int) Math.round(daily.getTemp().getMin()));
            vo.setMaxTemp((int) Math.round(daily.getTemp().getMax()));
            vo.setRainPercent((int) Math.round(daily.getPop() * 100));
            vo.setWindSpeed((int) Math.round(daily.getWindSpeed()));
            vo.setCloudPercent(daily.getClouds());

            if (daily.getWeather() != null && !daily.getWeather().isEmpty()) {
                vo.setWeatherText(daily.getWeather().get(0).getDescription());
                vo.setIconCode(daily.getWeather().get(0).getIcon());
            }

            vo.setUpdatedAt(LocalDateTime.now());

            weatherForecastMapper.upsertForecast(vo);
        }
    }

    /**
     * 관리자 / 스케줄러용 강제 갱신
     */
    public void forceUpdate(Integer regionId, double lat, double lon) {
        updateForecast(regionId, lat, lon);
    }
}
