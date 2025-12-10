package com.insightcrew.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.service.MemberService;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberservice;
	
	//회원정보 뷰, 회원정보 조회
	@GetMapping("/memberinfo")
	public String membepage() {
		return "member/member-info";
	}
	
	//회원정보 조회
	@PostMapping("/memberinfo")
	public String memberinfo(Authentication authentication, Model model) {
		String userid = authentication.getName();
		MemberVo membervo = memberservice.getMemberByUserid(membervo.getUserid());
		model.addAttribute("member", membervo);
		
		return "member/member-info";
	}
	
	//회원정보 수정
	@GetMapping("/membermod")
	public String membermod() {
		return
	}
	//회원탈퇴
	@GetMapping("/inactive")
	public String inactive() {
		return new String();
	}
	
}
