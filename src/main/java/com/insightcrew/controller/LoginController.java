package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class LoginController {
	
	//로그인 처리 요청은 form의 post로...
	//화면 요청이라 get매핑
	@GetMapping("/view")
	public String loginPage() {
		System.out.println("로그인 페이지 입니당");
		// html파일이 컨트롤러 폴더 내부에 있는 게 아니라면 원래 파일명으로.
		return "auth/auth-login";
	}
	
	@PostMapping("/login-process")
	public String login() {
		System.out.println("로그인 버튼 눌렀음");
		return "/member/member-mypage";
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout() {
		System.out.println("로그아웃 버튼 눌렀다잉");
		return "auth/auth-login";
	}
}
