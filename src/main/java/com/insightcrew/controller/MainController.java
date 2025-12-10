package com.insightcrew.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.insightcrew.domain.trip.dto.TripRankingDto;
import com.insightcrew.repository.TripRegionMapper;
import com.insightcrew.service.TripRankingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final TripRankingService rankingService;
    private final TripRegionMapper regionMapper;

    @GetMapping("/")
    public String main(
            @RequestParam(name = "sido", required = false) String sido,
            @RequestParam(name = "sigungu", required = false) String sigungu,
            Model model) {

        // 📍 시도 전체 목록 (드롭다운)
        List<String> sidoList = regionMapper.findDistinctSido();
        model.addAttribute("sidoList", sidoList);

        // 📍 시도 선택 → 시군구 목록 서버 렌더링용 (초기 진입 + 조회 버튼 누른 경우)
        if (sido != null && !sido.isEmpty()) {
            List<String> sigunguList = regionMapper.findSigunguBySido(sido);
            model.addAttribute("sigunguList", sigunguList);
        }

        // 선택 유지용
        model.addAttribute("selectedSido", sido);
        model.addAttribute("selectedSigungu", sigungu);

        // 🏆 메인 첫 진입용 기본 TOP5 (전국 기준)
        List<TripRankingDto> topTrips = rankingService.getTodayRanking();
        model.addAttribute("topTrips", topTrips);

        // TODO: 최신 게시글, 날씨 등은 세인이 쓰던 그대로 추가
        // model.addAttribute("latestPosts", ...);

        return "main/index";
    }

    // 임시 랭킹 캐시 테이블 생성
    @GetMapping("/admin/create-ranking")
    public String createRanking() {
        rankingService.updateDailyRanking();
        return "redirect:/";
    }
}