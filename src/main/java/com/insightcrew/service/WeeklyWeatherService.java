package com.insightcrew.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insightcrew.api.MidWeatherApiClient;
import com.insightcrew.domain.weather.dto.MidLandForecastItemDto;
import com.insightcrew.domain.weather.dto.MidTempForecastItemDto;
import com.insightcrew.domain.weather.dto.WeeklyWeatherDto;
import com.insightcrew.domain.weather.vo.WeatherWeeklyVo;
import com.insightcrew.repository.WeatherWeeklyMapper;
import com.insightcrew.util.WeatherDateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeeklyWeatherService {

    private final WeatherWeeklyMapper weeklyMapper;
    private final MidWeatherApiClient midWeatherApiClient;

    /* =====================================================
     * 0️⃣ 중기예보 캐시 실행 (배치 / 수동 공용)
     * ===================================================== */
    @Transactional
    public void updateWeeklyWeatherCache(String midRegionCode) {

        String tmFc = WeatherDateUtil.getMidForecastTime();

        MidLandForecastItemDto landDto =
                midWeatherApiClient.getLandForecast(midRegionCode, tmFc);

        MidTempForecastItemDto tempDto =
                midWeatherApiClient.getTempForecast(midRegionCode, tmFc);

        if (landDto == null || tempDto == null) return;

        updateWeeklyWeather(midRegionCode, landDto, tempDto);
    }

    /* =====================================================
     * 1️⃣ 중기예보 캐시 저장
     * ===================================================== */
    @Transactional
    public void updateWeeklyWeather(
            String midRegionCode,
            MidLandForecastItemDto landDto,
            MidTempForecastItemDto tempDto
    ) {
        weeklyMapper.deleteByRegion(midRegionCode);

        List<WeatherWeeklyVo> weeklyList =
                buildWeeklyWeather(midRegionCode, landDto, tempDto);

        for (WeatherWeeklyVo vo : weeklyList) {
            weeklyMapper.insert(vo);
        }
    }

    /* =====================================================
     * 2️⃣ 중기예보 API → VO 변환
     *  - D+4 ~ D+6 (단기 3일 + 중기 3일 = 총 6일 구성)
     * ===================================================== */
    public List<WeatherWeeklyVo> buildWeeklyWeather(
            String midRegionCode,
            MidLandForecastItemDto land,
            MidTempForecastItemDto temp
    ) {
        List<WeatherWeeklyVo> result = new ArrayList<>();

        LocalDate baseDate = WeatherDateUtil.getMidForecastBaseDate();

        for (int day = 4; day <= 6; day++) {
            WeatherWeeklyVo vo = new WeatherWeeklyVo();

            vo.setRegionCode(midRegionCode);
            vo.setForecastDate(baseDate.plusDays(day));

            Integer max = getTempMax(temp, day);
            Integer min = getTempMin(temp, day);

            vo.setTempHigh(max != null ? BigDecimal.valueOf(max) : null);
            vo.setTempLow(min != null ? BigDecimal.valueOf(min) : null);

            vo.setSkyAm(getSkyAm(land, day));
            vo.setSkyPm(getSkyPm(land, day));

            vo.setRainAm(getRainAm(land, day));
            vo.setRainPm(getRainPm(land, day));

            result.add(vo);
        }

        return result;
    }

    /* =====================================================
     * 3️⃣ 중기예보 캐시 → 화면 DTO
     * ===================================================== */
    public List<WeeklyWeatherDto> getWeeklyWeather(String midRegionCode) {

        return weeklyMapper.findByRegion(midRegionCode).stream().map(vo -> {
            WeeklyWeatherDto dto = new WeeklyWeatherDto();

            dto.setDate(vo.getForecastDate());

            dto.setTempHigh(
                    vo.getTempHigh() != null ? vo.getTempHigh().intValue() : null
            );

            dto.setTempLow(
                    vo.getTempLow() != null ? vo.getTempLow().intValue() : null
            );

            dto.setSky(
                    vo.getSkyPm() != null ? vo.getSkyPm() : vo.getSkyAm()
            );

            dto.setRain(
                    Math.max(
                            vo.getRainAm() != null ? vo.getRainAm() : 0,
                            vo.getRainPm() != null ? vo.getRainPm() : 0
                    )
            );

            return dto;
        }).toList();
    }
    
    @Transactional
    public void updateAllWeeklyWeatherCache(List<String> midRegionCodes) {

        for (String code : midRegionCodes) {
            updateWeeklyWeatherCache(code);
        }
    }

    /* =====================================================
     * 4️⃣ 헬퍼
     * ===================================================== */

    private Integer getTempMax(MidTempForecastItemDto temp, int day) {
        return switch (day) {
            case 4 -> parse(temp.getTaMax4());
            case 5 -> parse(temp.getTaMax5());
            case 6 -> parse(temp.getTaMax6());
            default -> null;
        };
    }

    private Integer getTempMin(MidTempForecastItemDto temp, int day) {
        return switch (day) {
            case 4 -> parse(temp.getTaMin4());
            case 5 -> parse(temp.getTaMin5());
            case 6 -> parse(temp.getTaMin6());
            default -> null;
        };
    }

    private String getSkyAm(MidLandForecastItemDto land, int day) {
        return switch (day) {
            case 4 -> land.getWf4Am();
            case 5 -> land.getWf5Am();
            case 6 -> land.getWf6Am();
            default -> null;
        };
    }

    private String getSkyPm(MidLandForecastItemDto land, int day) {
        return switch (day) {
            case 4 -> land.getWf4Pm();
            case 5 -> land.getWf5Pm();
            case 6 -> land.getWf6Pm();
            default -> null;
        };
    }

    private Integer getRainAm(MidLandForecastItemDto land, int day) {
        return switch (day) {
            case 4 -> land.getRnSt4Am();
            case 5 -> land.getRnSt5Am();
            case 6 -> land.getRnSt6Am();
            default -> null;
        };
    }

    private Integer getRainPm(MidLandForecastItemDto land, int day) {
        return switch (day) {
            case 4 -> land.getRnSt4Pm();
            case 5 -> land.getRnSt5Pm();
            case 6 -> land.getRnSt6Pm();
            default -> null;
        };
    }

    private Integer parse(String value) {
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
