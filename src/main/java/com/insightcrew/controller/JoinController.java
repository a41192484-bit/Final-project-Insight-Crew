package com.insightcrew.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.insightcrew.domain.member.dto.MemberJoinRequestDto;
import com.insightcrew.domain.member.dto.MemberJoinResponseDto;
import com.insightcrew.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class JoinController {
	private final MemberService memberService;

	//회원가입 화면
	@GetMapping("/join")
	public String joinPage() {
		return "auth/auth-join";
	}
	
	//가입하기
	@PostMapping("/join")
	public String join(MemberJoinRequestDto requestdto, Model model) {
		
		MemberJoinResponseDto res = memberService.join(requestdto);
		model.addAttribute("message", res.getMessage());
		
		if(res.isSuccess()) {
			return "redirect:/member/member-mypage";
		}else {
			return "auth/auth-join";
		}
		
		//바로 auth-login을 리턴하면 여전히 post 상태이기 때문에 브라우저 새로고침 시 post 재전송 경고 발생.
		//새로고침 안전하게 하기 위해서 redirect 사용.
	}
	
	//아이디 중복체크
	@GetMapping("/check-id")
	@ResponseBody
	public Map<String, Boolean> checkId(@RequestParam String userid){
		boolean exists = memberService.existsByUserid(userid);
		return Map.of("exists",exists);
	}
	
	//닉넨임 중복체크
	@GetMapping("/check-nick")
	@ResponseBody
	public Map<String, Boolean> checkNickname(@RequestParam String nickname){
		boolean exists = memberService.existsByNickname(nickname);
		return Map.of("exists",exists);
	}
}
