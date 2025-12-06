package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class LoginController {
	
	//화면 요청이라 get. 로그인 처리 요청은 post.
	@GetMapping("/login")
	public String loginPage() {
		// html이 컨트롤러 폴더 내부에 있는 게 아니라면 원래 파일명으로.
		return "auth-login";
	}
	
}
