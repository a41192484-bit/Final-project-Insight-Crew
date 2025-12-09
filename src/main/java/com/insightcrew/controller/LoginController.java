package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class LoginController {
	
	//화면 요청이라 get매핑
	@GetMapping("/login")
	public String loginPage() {
		// html파일이 컨트롤러 폴더 내부에 있는 게 아니라면 원래 파일명으로.
		return "auth/auth-login";
	}

	//로그인 처리 요청은 form의 post로...
	//그래서 로그인 컨트롤러에는 뷰 요청밖에 없나부다...
	
	
	//로그아웃
}
