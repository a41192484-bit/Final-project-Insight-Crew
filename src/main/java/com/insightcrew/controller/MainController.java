//package com.insightcrew.controller;
//
//import java.util.List;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import com.insightcrew.domain.trip.dto.TripRankingDto;
//import com.insightcrew.service.TripRankingService;
//
//import lombok.RequiredArgsConstructor;
//
//@Controller
//@RequiredArgsConstructor
//public class MainController {
//
//    private final TripRankingService rankingService;
//
//    @GetMapping("/") public String main(Model model) { 
//    	// 🌟 전국 기반 TOP5 추천 (기본 정책) 
//    	List<TripRankingDto> topTrips = rankingService.getTop5(); 
//    	model.addAttribute("topTrips", topTrips); 
//    	
//    	// ⭐ 만약 오늘 랭킹 캐시가 비어있으면 fallback 로직 (선택) 
//    	if (topTrips == null || topTrips.isEmpty()) { 
//    		model.addAttribute("topTrips", 
//    				rankingService.getTodayRanking()); 
//    		} 
//    	
//    	return "main/index"; }
//    }

package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.insightcrew.service.TripRankingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final TripRankingService rankingService;

    @GetMapping("/")
    public String main(Model model) {

        // ✅ 메인에서는 캐시된 TOP5만 사용 (가볍고 빠름)
        model.addAttribute("topTrips", rankingService.getTodayRanking());

        return "main/index";
    }
}
