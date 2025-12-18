package com.insightcrew.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.insightcrew.domain.board.dto.BoardListResponseDto;
import com.insightcrew.domain.trip.dto.TripRankingDto;
import com.insightcrew.domain.trip.vo.RegionVo;
import com.insightcrew.domain.weather.vo.WeatherForecastVo;
import com.insightcrew.repository.TripRegionMapper;
import com.insightcrew.service.TripRankingService;
import com.insightcrew.service.WeatherForecastService;
import com.insightcrew.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final TripRankingService rankingService;
    private final WeatherForecastService weatherForecastService;
    private final TripRegionMapper regionMapper;
    private final BoardService boardService;

    @GetMapping("/")
    public String main(
            @RequestParam(name = "sido", required = false) String sido,
            @RequestParam(name = "sigungu", required = false) String sigungu,
            Model model
    ) {

        /* =================================================
         * 0 시/도 목록
         * ================================================= */
        model.addAttribute("sidoList", regionMapper.findDistinctSido());

        /* =================================================
         * 1 기본 지역 처리
         * ================================================= */
        if (sido == null || sido.isBlank()) sido = "서울특별시";
        if (sigungu == null || sigungu.isBlank()) sigungu = "중구";

        model.addAttribute("selectedSido", sido);
        model.addAttribute("selectedSigungu", sigungu);

        /* =================================================
         * 2 시군구 목록
         * ================================================= */
        List<String> sigunguList = regionMapper.findSigunguBySido(sido);
        model.addAttribute("sigunguList", sigunguList);

        /* =================================================
         * 3 지역 정보 조회
         * ================================================= */
        RegionVo region = regionMapper.findBySidoAndSigungu(sido, sigungu);
        if (region == null) {
            region = regionMapper.findBySidoAndSigungu("서울특별시", "중구");
        }

        Integer regionId = region.getId();
        double lat = region.getLat();
        double lon = region.getLon();

        /* =================================================
         * 4 오늘의 여행지 TOP5 랭킹
         * ================================================= */
        List<TripRankingDto> rankingList = rankingService.getTodayRanking();
        model.addAttribute("rankingList", rankingList);

        /* =================================================
         * 5 주간 날씨 (오늘 포함 7일)
         * ================================================= */
        List<WeatherForecastVo> forecastList =
                weatherForecastService.getForecast(regionId);
        model.addAttribute("forecastList", forecastList);

        /* =================================================
         * 6 오늘 날씨
         * ================================================= */
        WeatherForecastVo todayWeather =
                weatherForecastService.getToday(regionId);
        model.addAttribute("todayWeather", todayWeather);
        
        /* =================================================
         * 7 커뮤니티 최신글 (자유게시판)
         * ================================================= */
        List<BoardListResponseDto> latestBoardList =
                boardService.findLatestPosts(5);
        model.addAttribute("latestBoardList", latestBoardList);

        return "main/index";
    }
}
