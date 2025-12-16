package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.weather.vo.WeatherTodayVo;

@Mapper
public interface TripWeatherTodayMapper {

    // UPSERT (region_id 기반)
    int upsertTodayWeather(WeatherTodayVo vo);

    // region_id 로 단일 조회
    WeatherTodayVo findByRegionId(@Param("regionId") Integer regionId);

    // 전체 조회
    List<WeatherTodayVo> findAll();
}
