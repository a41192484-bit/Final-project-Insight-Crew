package com.insightcrew.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.insightcrew.domain.trip.vo.RegionVo;
import com.insightcrew.domain.weather.dto.TodayWeatherDto;
import com.insightcrew.domain.weather.dto.WeeklyWeatherDto;
import com.insightcrew.repository.TripRegionMapper;
import com.insightcrew.service.TodayWeatherService;
import com.insightcrew.service.TripRankingService;
import com.insightcrew.service.WeeklyWeatherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final TripRankingService rankingService;
    private final WeeklyWeatherService weeklyWeatherService;
    private final TodayWeatherService todayWeatherService;
    private final TripRegionMapper regionMapper;

    @GetMapping("/")
    public String main(
            @RequestParam(name = "sido", required = false) String sido,
            @RequestParam(name = "sigungu", required = false) String sigungu,
            Model model
    ) {

        /* =========================
         * 0️⃣ 시/도 목록
         * ========================= */
        model.addAttribute("sidoList", regionMapper.findDistinctSido());

        /* =========================
         * 1️⃣ 기본 지역 처리
         * ========================= */
        if (sido == null || sido.isBlank()) sido = "서울특별시";
        if (sigungu == null || sigungu.isBlank()) sigungu = "중구";

        model.addAttribute("selectedSido", sido);
        model.addAttribute("selectedSigungu", sigungu);

        /* =========================
         * 2️⃣ 지역 정보 조회
         * ========================= */
        RegionVo region = regionMapper.findBySidoAndSigungu(sido, sigungu);
        if (region == null) {
            region = regionMapper.findBySidoAndSigungu("서울특별시", "중구");
        }

        int nx = region.getNx();
        int ny = region.getNy();
        String midRegionCode = region.getMidRegionCode();

        /* =========================
         * 3️⃣ 오늘의 여행지 TOP5
         * ========================= */
        model.addAttribute("rankingList", rankingService.getTodayRanking());

        /* =========================
         * 4️⃣ 오늘 날씨
         * ========================= */
        TodayWeatherDto todayWeather =
                todayWeatherService.getTodayWeather(nx, ny);
        model.addAttribute("todayWeather", todayWeather);

        /* =========================
         * 5️⃣ 주간 날씨 (🔥 최종 로직)
         *  - 단기 D+1 ~ D+3
         *  - 중기 D+4 ~ D+6
         *  - 데이터 없어도 6칸 고정
         *  - 날짜 기준 정렬
         * ========================= */
        List<WeeklyWeatherDto> weeklyResult = new ArrayList<>();

        // 단기예보 map (date 기준)
        Map<LocalDate, WeeklyWeatherDto> shortTermMap =
                todayWeatherService.getShortTerm(nx, ny).stream()
                        .collect(Collectors.toMap(
                                WeeklyWeatherDto::getDate,
                                d -> d
                        ));

        // 중기예보 map (date 기준)
        Map<LocalDate, WeeklyWeatherDto> midTermMap =
                weeklyWeatherService.getWeeklyWeather(midRegionCode).stream()
                        .collect(Collectors.toMap(
                                WeeklyWeatherDto::getDate,
                                d -> d
                        ));

        // 기준일: 내일
        LocalDate baseDate = LocalDate.now().plusDays(1);

        // 🔥 무조건 6일 생성
        for (int i = 0; i < 6; i++) {
            LocalDate targetDate = baseDate.plusDays(i);

            WeeklyWeatherDto dto =
                    shortTermMap.getOrDefault(
                            targetDate,
                            midTermMap.getOrDefault(
                                    targetDate,
                                    new WeeklyWeatherDto()
                            )
                    );

            dto.setDate(targetDate); // 날짜는 무조건 세팅
            weeklyResult.add(dto);
        }

        // 날짜 기준 정렬 (요일 꼬임 방지)
        weeklyResult.sort(Comparator.comparing(WeeklyWeatherDto::getDate));

        model.addAttribute("weeklyWeather", weeklyResult);

        return "main/index";
    }
}
