package com.insightcrew.util;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.repository.MemberMapper;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class PasswordMigrationUtil {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	@GetMapping("/convert-password")
	public String convertPassword() {
		
		//모든 회원 조회
		List<MemberVo> memberList = memberMapper.findAll();
		
		for(MemberVo membervo : memberList) {
			String oldPW = membervo.getPassword();
			
			// 이미 BCrypt인지 확인 ($2a$ 또는 $2b$)
			if(!oldPW.startsWith("$2a$") && !oldPW.startsWith("$2b$")){
				
				//일반 텍트스 비번을 BCrypt로 암호화
				String encoded = passwordEncoder.encode(oldPW);
				memberMapper.updatePassword(membervo.getUserid(), encoded);
				System.out.println(membervo.getUserid()+"비밀번호 변환 완료");
			}else {
				System.out.println("이미 암호화 됨: "+membervo.getUserid());
			}
			
		}
		System.out.println("여기는 패스워드 인코더 클래스");
		return "비밀번호 변환 완료!";
	}
}