package com.insightcrew.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.weather.vo.WeatherTodayVo;

@Mapper
public interface TripWeatherTodayMapper {

    // 오늘 날씨 저장 (중복 있을 경우 update)
    int upsertTodayWeather(WeatherTodayVo vo);

    // 특정 region_code로 오늘 날씨 가져오기
    WeatherTodayVo findByRegion(@Param("regionCode") String regionCode);
}
