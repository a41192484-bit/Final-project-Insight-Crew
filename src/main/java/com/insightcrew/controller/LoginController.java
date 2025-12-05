package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class LoginController {
	
	//화면 요청이라 get. 로그인 처리 요청은 post.
	@GetMapping("/login")
	public String loginPage() {
		return "auth/auth-login";
	}
	
}
