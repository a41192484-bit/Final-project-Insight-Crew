package com.insightcrew.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.insightcrew.domain.member.dto.MemberJoinRequestDto;
import com.insightcrew.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class JoinController {
	private final MemberService memberService;
	
	//뷰 불러옴
	@GetMapping("/auth-join")
	public String joinForm() {
		return "auth/join";
	}
	
	//가입
	@PostMapping("/join")
	public String join(MemberJoinRequestDto dto) {
		memberService.join(dto);
		return "redirect: /auth/login ? joinSuccess=true";
	}
	
	//아이디 중복체크
	@GetMapping("/check-id")
	@ResponseBody
	public Map<String, Boolean> checkId(@RequestParam String userid){
		boolean exists = memberService.existsByUserid(userid);
		return Map.of("exists",exists);
	}
	
	//닉넨임 중복체크
	public Map<String, Boolean> checkNickname(@RequestParam String nickname){
		boolean exists = memberService.existsByNickname(nickname);
		return Map.of("exists",exists);
	}
}
