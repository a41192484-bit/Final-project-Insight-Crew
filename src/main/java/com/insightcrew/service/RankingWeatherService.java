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

    /** ============================
     *   전체 지역 단기예보 업데이트
     *  ============================ */
    public void updateAllRegionsWeather() {

        List<RegionVo> regions = tripRegionMapper.findAll();

        for (RegionVo r : regions) {

            List<WeatherItemDto> items = weatherClient.getWeather(r.getNx(), r.getNy());
            if (items == null || items.isEmpty()) {
                System.out.println("❌ 날씨 예보 데이터 없음 → regionId=" + r.getId());
                continue;
            }

            WeatherTodayVo vo = new WeatherTodayVo();
            vo.setRegionId(r.getId());

            // 🔥 온도는 T1H 없으면 TMP로 fallback
            String tempStr = get(items, "T1H", "TMP");
            vo.setTemp(parseDouble(tempStr));

            vo.setSky(parseInt(get(items, "SKY")));
            vo.setPty(parseInt(get(items, "PTY")));
            vo.setWind(parseDouble(get(items, "WSD")));

            weatherMapper.upsertTodayWeather(vo);
        }

        System.out.println("🌤 모든 지역 단기예보 업데이트 완료");
    }

    /** ============================
     *     category 에 맞는 값 추출 주어진 category 목록 중, 처음으로 발견된 값 하나를 돌려주는 메서드
     *  ============================ */
    private String get(List<WeatherItemDto> list, String... categories) {

        if (list == null || list.isEmpty()) return null;

        for (String category : categories) {
            String value = list.stream()
                    .filter(i -> i != null)
                    .filter(i -> i.getCategory() != null)
                    .filter(i -> i.getCategory().equals(category))
                    .map(WeatherItemDto::getValue)  // obsrValue / fcstValue 중 있는 값
                    .filter(v -> v != null && !v.isBlank())
                    .findFirst()
                    .orElse(null);

            if (value != null) return value;
        }

        return null;
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
