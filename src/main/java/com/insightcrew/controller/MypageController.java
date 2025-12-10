package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/mypage")
public class MypageController {
	
	//마이페이지 뷰
	@GetMapping("/view")
	public String mypage() {
		return "member/member-mypage";
	}

}
