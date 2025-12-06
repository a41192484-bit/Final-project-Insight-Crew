package com.insightcrew.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.insightcrew.api.TripApiWeatherClient;
import com.insightcrew.domain.weather.dto.WeatherItemDto;
import com.insightcrew.domain.weather.vo.WeatherTodayVo;
import com.insightcrew.repository.TripWeatherTodayMapper;
import com.insightcrew.util.GridConverter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripWeatherCacheService {

    private final TripApiWeatherClient weatherClient;
    private final TripWeatherTodayMapper weatherTodayMapper;

    /**
     * 위도/경도를 받아서
     * 1) nx, ny 좌표 계산
     * 2) 기상청 API 호출
     * 3) weather_today_cache UPSERT
     * 4) 저장된 WeatherTodayVo 반환
     */
    public WeatherTodayVo updateTodayWeather(double lat, double lon) {

        int[] grid = GridConverter.toGrid(lat, lon);
        int nx = grid[0];
        int ny = grid[1];

        String regionKey = nx + "-" + ny;

        List<WeatherItemDto> items = weatherClient.getWeather(nx, ny);

        if (items == null || items.isEmpty()) {
            return null;
        }

        WeatherTodayVo vo = new WeatherTodayVo();
        vo.setRegionCode(regionKey);
        vo.setTemp(parseDouble(getValue(items, "T1H")));
        vo.setSky(parseInt(getValue(items, "SKY")));
        vo.setPty(parseInt(getValue(items, "PTY")));
        vo.setWind(parseDouble(getValue(items, "WSD")));

        weatherTodayMapper.upsertTodayWeather(vo);

        // DB에 실제 저장된 값 다시 조회해서 반환 (updatedAt 포함)
        return weatherTodayMapper.findByRegion(regionKey);
    }

    /**
     * 캐시 VO → 기존 점수계산 서비스가 쓰는 WeatherItemDto 리스트로 변환
     */
    public List<WeatherItemDto> convertToWeatherItems(WeatherTodayVo vo) {

        if (vo == null) {
            return List.of();
        }

        List<WeatherItemDto> list = new ArrayList<>();

        if (vo.getPty() != null) {
            WeatherItemDto pty = new WeatherItemDto();
            pty.setCategory("PTY");
            pty.setFcstValue(String.valueOf(vo.getPty()));
            list.add(pty);
        }

        if (vo.getSky() != null) {
            WeatherItemDto sky = new WeatherItemDto();
            sky.setCategory("SKY");
            sky.setFcstValue(String.valueOf(vo.getSky()));
            list.add(sky);
        }

        if (vo.getTemp() != null) {
            WeatherItemDto t1h = new WeatherItemDto();
            t1h.setCategory("T1H");
            t1h.setFcstValue(String.valueOf(vo.getTemp().intValue())); // 정수로 사용
            list.add(t1h);
        }

        if (vo.getWind() != null) {
            WeatherItemDto wsd = new WeatherItemDto();
            wsd.setCategory("WSD");
            wsd.setFcstValue(String.valueOf(vo.getWind()));
            list.add(wsd);
        }

        return list;
    }

    private String getValue(List<WeatherItemDto> items, String category) {
        return items.stream()
                .filter(i -> category.equals(i.getCategory()))
                .map(WeatherItemDto::getFcstValue)
                .findFirst()
                .orElse(null);
    }

    private Double parseDouble(String v) {
        try {
            return (v == null || v.isBlank()) ? null : Double.parseDouble(v);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInt(String v) {
        try {
            return (v == null || v.isBlank()) ? null : Integer.parseInt(v);
        } catch (Exception e) {
            return null;
        }
    }
}
