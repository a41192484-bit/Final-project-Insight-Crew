package com.insightcrew.controller;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insightcrew.domain.trip.dto.TripRankingDto;
import com.insightcrew.service.TripRankingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class TripRankingApiController {

    private final TripRankingService rankingService;

    // 지역별 날씨 기반 TOP5
    @GetMapping("/weather")
    public List<TripRankingDto> getRankingByRegion(
            @RequestParam String sido,
            @RequestParam String sigungu
    ) {
        return rankingService.getRankingByRegion(sido, sigungu);
    }
}
