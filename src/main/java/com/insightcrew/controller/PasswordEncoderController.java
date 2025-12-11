package com.insightcrew.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.repository.MemberRepository;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class PasswordEncoderController {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/convert-password")
	public String convertPassword() {
		
		//모든 회원 조회
		List<MemberVo> memberList = memberRepository.findAll();
		
		for(MemberVo membervo : memberList) {
			String oldPW = membervo.getPassword();
			
			if(oldPW.startsWith("$2a$") || oldPW.startsWith("$2b$")){
				continue;
			}
			
			String encoded = passwordEncoder.encode(oldPW);
			memberRepository.updatePassword(membervo.getUserid(), encoded);
		}
		
		return "비밀번호 변환 완료!";
	}
}
