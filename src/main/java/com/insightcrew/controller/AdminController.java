package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    // 나중에 서비스 주입 (지금은 안 넣어도 됨)

    @GetMapping
    public String adminMain(Model model) {

        // 관리자 대시보드 통계 (임시값 or 실제값)
        model.addAttribute("tripCount", 0);
        model.addAttribute("todayWeatherStatus", "정상");
        model.addAttribute("rankingStatus", "완료");

        return "admin/main";
    }
}
