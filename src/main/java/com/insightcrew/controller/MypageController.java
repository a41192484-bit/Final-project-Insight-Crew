package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/mypage")
public class MypageController {
	
	//마이페이지 뷰
	@GetMapping("/mypage")
	public String mypage() {
		return "member/member-mypage";
	}
	
	//회원정보관리
	@GetMapping("/member")
	public String memberInfo() {
		return "member/member-info";
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout() {
		return "auth/auth-login";
	}
	
	//회원탈퇴
	@GetMapping("/inactive")
	public String inactive() {
		return new String();
	}
	
}
