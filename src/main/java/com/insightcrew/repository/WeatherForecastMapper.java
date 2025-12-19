package com.insightcrew.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.weather.vo.WeatherForecastVo;

public interface WeatherForecastMapper {
	
	int countToday();

    /**
     * 오늘 날씨 (forecast 기준)
     * - regionId 기준
     * - 가장 가까운 1건
     */
    WeatherForecastVo findTodayByRegion(
        @Param("regionId") Integer regionId
    );

    /**
     * 주간 날씨 (오늘 포함 7일)
     */
    List<WeatherForecastVo> findWeeklyByRegion(
        @Param("regionId") Integer regionId
    );

    /**
     * 24시간 이내 캐시 존재 여부
     */
    boolean existsRecentCache(
        @Param("regionId") Integer regionId,
        @Param("threshold") LocalDateTime threshold
    );

    /**
     * OpenWeather 예보 upsert
     * - regionId + weatherDate 기준
     */
    void upsertForecast(
        WeatherForecastVo vo
    );

    /**
     * 지역별 캐시 삭제
     */
    void deleteByRegion(
        @Param("regionId") Integer regionId
    );
}