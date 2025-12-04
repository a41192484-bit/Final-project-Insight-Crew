package com.insightcrew.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.insightcrew.domain.trip.vo.TripVo;
import com.insightcrew.service.TripService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    // 여행지 목록 + 페이징
    @GetMapping("/trip/list")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       Model model) {

        int size = 16;
        int pageCount = 10;  // 한 번에 보여줄 페이지 버튼 수 = 10

        // 페이지 데이터
        List<TripVo> tripList = tripService.getTripPage(page, size);
        int totalPages = tripService.getTotalPages(size);

        // 페이지 그룹 계산
        int startPage = ((page - 1) / pageCount) * pageCount + 1;
        int endPage = Math.min(startPage + pageCount - 1, totalPages);

        model.addAttribute("tripList", tripList);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "trip/trip-list";
    }

    // 여행지 상세보기
    @GetMapping("/trip/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("trip", tripService.findById(id));
        return "trip/trip-detail";
    }
}