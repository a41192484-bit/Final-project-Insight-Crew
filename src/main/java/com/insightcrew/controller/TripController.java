package com.insightcrew.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.insightcrew.domain.trip.dto.TripDetailResponse;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.service.TripRankingService;
import com.insightcrew.service.TripService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final TripRankingService tripRankingService;

    // 여행지 목록 + 검색 + 카테고리 + 페이징 + 지역기반 랭킹
    @GetMapping("/trip/list")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "category", required = false) String category,
                       @RequestParam(name = "region", required = false) String region,   // ★ 추가
                       Model model) {

        int size = 16;       // 한 페이지에 16개
        int pageCount = 10;  // 페이징 그룹

        // null 방지 처리
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = null;
        }
        if (category == null || category.trim().isEmpty()) {
            category = null;
        }
        if (region != null && region.trim().isEmpty()) {
            region = null;
        }

        // 검색 + 카테고리 + 페이징 목록
        List<TripVo> tripList = tripService.searchTrips(keyword, category, page, size);

        // 전체 페이지 수
        int totalPages = tripService.getSearchTotalPages(keyword, category, size);

        // 페이지 그룹 계산
        int startPage = ((page - 1) / pageCount) * pageCount + 1;
        int endPage = Math.min(startPage + pageCount - 1, totalPages);

        // 지역 리스트 (드롭다운용)
        model.addAttribute("regions", tripService.getRegionList());
        model.addAttribute("region", region);

        // ⭐ 지역 필터가 있으면 지역별 TOP5, 없으면 오늘의 TOP5
        if (region == null) {
            model.addAttribute("rankingTop5", tripRankingService.getTodayRanking());
        } else {
            model.addAttribute("rankingTop5", tripRankingService.getTodayRankingByRegion(region));
        }

        // 화면 전달
        model.addAttribute("tripList", tripList);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 검색/카테고리 유지
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        return "trip/trip-list";
    }

    // 여행지 상세보기
    @GetMapping("/trip/detail/{id}")
    public String tripDetail(@PathVariable("id") Long id, Model model) {

        TripDetailResponse detail = tripService.getTripDetail(id);
        model.addAttribute("detail", detail);

        return "trip/trip-detail";
    }
}