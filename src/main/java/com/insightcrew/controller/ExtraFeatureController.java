package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 서비스 확장용
@Controller
@RequestMapping("/inquiry")
public class ExtraFeatureController {

    // ===============================
    // 1:1 문의 목록
    // ===============================
    @GetMapping("/list")
    public String inquiryList() {
        return "inquiry/inquiry-list";   // templates/inquiry/list.html
    }

    // ===============================
    // Q&A 목록
    // ===============================
    @GetMapping("/qna")
    public String qnaList() {
        return "inquiry/inquiry-qna";    // templates/inquiry/qna.html
    }
}
