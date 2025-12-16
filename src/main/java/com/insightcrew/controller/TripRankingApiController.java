//package com.insightcrew.controller;
//
//import java.util.List;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.insightcrew.domain.trip.dto.TripRankingDto;
//import com.insightcrew.service.TripRankingService;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/ranking")
//public class TripRankingApiController {
//
//    private final TripRankingService rankingService;
//
//    // 전국 랭킹 TOP5 제공
//    @GetMapping("/top5")
//    public List<TripRankingDto> getTop5Ranking() {
//        return rankingService.getTop5();
//    }
//}
