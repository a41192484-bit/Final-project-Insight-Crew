package com.insightcrew.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.insightcrew.api.TripApiWeatherClient;
import com.insightcrew.domain.trip.vo.RegionVo;
import com.insightcrew.domain.weather.dto.WeatherItemDto;
import com.insightcrew.domain.weather.vo.WeatherTodayVo;
import com.insightcrew.repository.TripRegionMapper;
import com.insightcrew.repository.TripWeatherTodayMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingWeatherService {

    private final TripRegionMapper tripRegionMapper;
    private final TripApiWeatherClient weatherClient;
    private final TripWeatherTodayMapper weatherMapper;

    /** 전체 시·도/시군구 날씨 업데이트 */
    public void updateAllRegionsWeather() {

        List<RegionVo> regions = tripRegionMapper.findAll();

        for (RegionVo r : regions) {

            List<WeatherItemDto> items = weatherClient.getWeather(r.getNx(), r.getNy());
            if (items == null || items.isEmpty()) continue;

            WeatherTodayVo vo = new WeatherTodayVo();
            vo.setRegionId(r.getId());
            vo.setTemp(parseDouble(get(items, "T1H")));
            vo.setSky(parseInt(get(items, "SKY")));
            vo.setPty(parseInt(get(items, "PTY")));
            vo.setWind(parseDouble(get(items, "WSD")));

            weatherMapper.upsertTodayWeather(vo);
        }

        System.out.println("🌤 지역 기반 현재날씨 업데이트 완료");
    }

    private String get(List<WeatherItemDto> list, String category) {
        return list.stream()
                .filter(i -> category.equals(i.getCategory()))
                .map(WeatherItemDto::getFcstValue)
                .findFirst()
                .orElse(null);
    }

    private Double parseDouble(String v) {
        try { return (v == null) ? null : Double.parseDouble(v); }
        catch (Exception e) { return null; }
    }

    private Integer parseInt(String v) {
        try { return (v == null) ? null : Integer.parseInt(v); }
        catch (Exception e) { return null; }
    }
}
