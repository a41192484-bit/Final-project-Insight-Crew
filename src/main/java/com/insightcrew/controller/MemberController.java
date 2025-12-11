package com.insightcrew.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.insightcrew.config.CustomUserDetails;
import com.insightcrew.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberservice;
	
	//회원정보 조회, 뷰
	@GetMapping("/info")
	public String memberinfo(Model model,@AuthenticationPrincipal CustomUserDetails user) {
		//커스텀 유저 디테일이 이미 유저의 모든 정보를 가지고 있음.
		model.addAttribute("user", user);
		return "member/member-info";
	}
	
	//회원정보 수정
	@PostMapping("/update")
	@ResponseBody
	public String updateinfo(@AuthenticationPrincipal CustomUserDetails user,
							@RequestBody Map<String, String> body) {
		
		String field = body.get("field");
		String value = body.get("value");
		
		memberservice.updateOneField(user.getUsername(), field, value);
		return "success";
	}
	//회원탈퇴
	@GetMapping("/inactive")
	public String inactive() {
		return "/";
	}
	
}
