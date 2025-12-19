package com.insightcrew.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.insightcrew.domain.trip.dto.TripDetailResponse;
import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.repository.TripRegionMapper;
import com.insightcrew.service.TripService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final TripRegionMapper regionMapper;

    // ============================================
    // 여행지 목록 + 검색 + 카테고리 + 지역 + 페이징
    // ============================================
    @GetMapping("/trip/list")
    public String list(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "sido", required = false) String sido,
            @RequestParam(name = "sigungu", required = false) String sigungu,
            Model model
    ) {

        int size = 16;
        int pageCount = 10;

        // ==== null 정리 ====
        keyword = normalize(keyword);
        category = normalize(category);
        sido = normalize(sido);
        sigungu = normalize(sigungu);

        // ==== 검색 결과 ====
        List<TripVo> tripList =
                tripService.searchTrips(keyword, category, sido, sigungu, page, size);

        int totalPages =
                tripService.getSearchTotalPages(keyword, category, sido, sigungu, size);

        // ==== 페이징 ====
        int startPage = ((page - 1) / pageCount) * pageCount + 1;
        int endPage = Math.min(startPage + pageCount - 1, totalPages);

        // ==== 지역 선택 목록 ====
        List<String> sidoList = regionMapper.findDistinctSido();
        List<String> sigunguList =
                (sido != null ? regionMapper.findSigunguBySido(sido) : List.of());

        // ==== 전달 ====
        model.addAttribute("tripList", tripList);

        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 검색 유지
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        // 지역 유지
        model.addAttribute("sidoList", sidoList);
        model.addAttribute("sigunguList", sigunguList);
        model.addAttribute("selectedSido", sido);
        model.addAttribute("selectedSigungu", sigungu);

        return "trip/trip-list";
    }

    // ============================================
    // 여행지 상세보기
    // ============================================
    @GetMapping("/trip/detail/{id}")
    public String tripDetail(@PathVariable("id") Long id, Model model) {

        TripDetailResponse detail = tripService.getTripDetail(id);
        model.addAttribute("detail", detail);

        return "trip/trip-detail";
    }

    // ============================================
    // 내부 메서드 (공백 = null 처리)
    // ============================================
    private String normalize(String s) {
        return (s != null && s.trim().isEmpty()) ? null : s;
    }
}
