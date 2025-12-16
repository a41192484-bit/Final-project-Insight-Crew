package com.insightcrew.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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

    /* =====================================================
     * 전체 지역 단기예보 업데이트
     * - 단기예보는 TMP 기반으로 1~3일 요약
     * ===================================================== */
    public void updateAllRegionsWeather() {

        List<RegionVo> regions = tripRegionMapper.findAll();

        for (RegionVo r : regions) {

            List<WeatherItemDto> items =
                    weatherClient.getWeather(r.getNx(), r.getNy());

            if (items == null || items.isEmpty()) continue;

            WeatherTodayVo vo = new WeatherTodayVo();
            vo.setRegionId(r.getId());

            /* =====================
             * 오늘 (실황)
             * ===================== */
            vo.setTemp(parseDouble(get(items, "T1H", "TMP")));
            vo.setSky(parseInt(get(items, "SKY")));
            vo.setPty(parseInt(get(items, "PTY")));
            vo.setWind(parseDouble(get(items, "WSD")));

            /* =====================
             * 단기예보 (+1 ~ +3)
             * ===================== */
            applyDailyForecast(vo, items, 1);
            applyDailyForecast(vo, items, 2);
            applyDailyForecast(vo, items, 3);

            weatherMapper.upsertTodayWeather(vo);
        }
    }

    /* =====================================================
     * 하루 단위 예보 적용
     * - TMX/TMN 우선
     * - 없으면 TMP로 계산
     * ===================================================== */
    private void applyDailyForecast(
            WeatherTodayVo vo,
            List<WeatherItemDto> items,
            int plusDay
    ) {
        String date =
                LocalDate.now().plusDays(plusDay)
                        .format(DateTimeFormatter.BASIC_ISO_DATE);

        // 1️⃣ TMX/TMN 우선
        Integer maxTemp = getMax(items, date, "TMX");
        Integer minTemp = getMin(items, date, "TMN");

        // 2️⃣ 없으면 TMP로 계산 (🔥 핵심 복구)
        if (maxTemp == null || minTemp == null) {
            maxTemp = getMax(items, date, "TMP");
            minTemp = getMin(items, date, "TMP");
        }

        // 진짜 데이터가 없는 날만 스킵
        if (maxTemp == null || minTemp == null) return;

        Integer maxRain = getMax(items, date, "POP");
        String sky = getLatestSky(items, date);

        if (plusDay == 1) {
            vo.setTomorrowHigh(maxTemp);
            vo.setTomorrowLow(minTemp);
            vo.setTomorrowRain(maxRain);
            vo.setTomorrowSky(sky);
        } else if (plusDay == 2) {
            vo.setDay2High(maxTemp);
            vo.setDay2Low(minTemp);
            vo.setDay2Rain(maxRain);
            vo.setDay2Sky(sky);
        } else if (plusDay == 3) {
            vo.setDay3High(maxTemp);
            vo.setDay3Low(minTemp);
            vo.setDay3Rain(maxRain);
            vo.setDay3Sky(sky);
        }
    }

    /* =====================================================
     * SKY 최신값
     * ===================================================== */
    private String getLatestSky(
            List<WeatherItemDto> items,
            String date
    ) {
        return items.stream()
                .filter(i -> "SKY".equals(i.getCategory()))
                .filter(i -> date.equals(i.getFcstDate()))
                .max(Comparator.comparing(WeatherItemDto::getFcstTime))
                .map(i -> convertSky(i.getValue()))
                .orElse(null);
    }

    /* =====================================================
     * 최대값 / 최소값
     * ===================================================== */
    private Integer getMax(
            List<WeatherItemDto> items,
            String date,
            String category
    ) {
        return items.stream()
                .filter(i -> date.equals(i.getFcstDate()))
                .filter(i -> category.equals(i.getCategory()))
                .map(i -> parseInt(i.getValue()))
                .filter(v -> v != null)
                .max(Integer::compareTo)
                .orElse(null);
    }

    private Integer getMin(
            List<WeatherItemDto> items,
            String date,
            String category
    ) {
        return items.stream()
                .filter(i -> date.equals(i.getFcstDate()))
                .filter(i -> category.equals(i.getCategory()))
                .map(i -> parseInt(i.getValue()))
                .filter(v -> v != null)
                .min(Integer::compareTo)
                .orElse(null);
    }

    /* =====================================================
     * 단순 category 조회
     * ===================================================== */
    private String get(List<WeatherItemDto> list, String... categories) {
        for (String category : categories) {
            String value = list.stream()
                    .filter(i -> category.equals(i.getCategory()))
                    .map(WeatherItemDto::getValue)
                    .filter(v -> v != null && !v.isBlank())
                    .findFirst()
                    .orElse(null);
            if (value != null) return value;
        }
        return null;
    }

    /* =====================================================
     * SKY 코드 변환
     * ===================================================== */
    private String convertSky(String skyCode) {
        if (skyCode == null) return null;
        return switch (skyCode) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> null;
        };
    }

    /* =====================================================
     * 파싱 유틸
     * ===================================================== */
    private Double parseDouble(String v) {
        try { return v == null ? null : Double.parseDouble(v); }
        catch (Exception e) { return null; }
    }

    private Integer parseInt(String v) {
        try { return v == null ? null : (int) Double.parseDouble(v); }
        catch (Exception e) { return null; }
    }
}
