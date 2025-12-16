package com.insightcrew.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.insightcrew.domain.weather.vo.WeatherWeeklyVo;

@Mapper
public interface WeatherWeeklyMapper {

    void deleteByRegion(String regionCode);

    void insert(WeatherWeeklyVo vo);

    List<WeatherWeeklyVo> findByRegion(String regionCode);
}
