package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.insightcrew.repository.BoardMapper;
import com.insightcrew.repository.MemberMapper;
import com.insightcrew.repository.TripMapper;
import com.insightcrew.repository.TripRankingCacheMapper;
import com.insightcrew.repository.WeatherForecastMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardController {

    private final MemberMapper memberMapper;
    private final TripMapper tripMapper;
    private final BoardMapper boardMapper;
    private final WeatherForecastMapper weatherForecastMapper;
    private final TripRankingCacheMapper tripRankingCacheMapper;

    @GetMapping
    public String dashboard(Model model) {

        int memberCount = memberMapper.countAll();
        int tripCount = tripMapper.countAll();
        int boardCount = boardMapper.countAll();

        String todayWeatherStatus =
                weatherForecastMapper.countToday() > 0 ? "정상" : "미갱신";

        String rankingStatus =
                tripRankingCacheMapper.countToday() > 0 ? "완료" : "미생성";

        model.addAttribute("memberCount", memberCount);
        model.addAttribute("tripCount", tripCount);
        model.addAttribute("boardCount", boardCount);
        model.addAttribute("todayWeatherStatus", todayWeatherStatus);
        model.addAttribute("rankingStatus", rankingStatus);

        return "admin/main";
    }
}
