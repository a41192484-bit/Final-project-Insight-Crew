package com.insightcrew.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.insightcrew.repository.TripRegionMapper;
import com.insightcrew.service.WeeklyWeatherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WeatherAdminController {

    private final WeeklyWeatherService weeklyWeatherService;
    private final TripRegionMapper regionMapper;

    @GetMapping("/admin/weather/weekly/update")
    public String updateWeeklyWeatherManually() {

        // ✅ 모든 중기 지역 코드 조회
        List<String> midRegionCodes =
                regionMapper.findDistinctMidRegionCodes();

        // ✅ 전지역 중기예보 캐시
        weeklyWeatherService.updateAllWeeklyWeatherCache(midRegionCodes);

        return "redirect:/";
    }
}
