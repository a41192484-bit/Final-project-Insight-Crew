package com.insightcrew.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insightcrew.domain.member.dto.MemberJoinRequestDto;
import com.insightcrew.domain.member.dto.MemberJoinResponseDto;
import com.insightcrew.domain.member.enums.UserRole;
import com.insightcrew.domain.member.enums.UserStatus;
import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.repositary.MemberRepositary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepositary memberRepositary;
	private final PasswordEncoder passwordEncoder;
	
	//아이디 조회
	public MemberVo findUser(String userid) {
		return memberRepositary.findByUserid(userid);
	}
	
	//아이디 중복체크
	public boolean existsByUserid(String userid) {
		//db에서 id를 조회했는데 null이 아니면 존재=중복임.
		return memberRepositary.findByUserid(userid) != null;
	}
	
	//닉네임 중복체크
	public boolean existsByNickname(String nickname) {
		if(nickname == null || nickname.isBlank()) {
			return false;
		}
        return memberRepositary.findByNickname(nickname) != null;
    }
	
	//회원가입
	public MemberJoinResponseDto join(MemberJoinRequestDto requestdto) {
		if(existsByUserid(requestdto.getUserid())) {
			return new MemberJoinResponseDto(false, "이미 사용 중인 아이디입니다.");
		}
		
		if(existsByNickname(requestdto.getNickname())) {
			return new MemberJoinResponseDto(false, "이미 사용 중인 닉네임입니다.");
		}
		
		//비밀번호 암호화. 감싸기.
		String encodedPw = passwordEncoder.encode(requestdto.getPassword());
		
		//memberVO 생성
		MemberVo vo = new MemberVo();
		
		vo.setUserid(requestdto.getUserid());
		vo.setPassword(encodedPw);
		vo.setName(requestdto.getName());
		vo.setNickname(requestdto.getNickname());
		vo.setRole(UserRole.USER);
		vo.setStatus(UserStatus.ACTIVE);
		
		memberRepositary.insertMember(vo);
		
		return new MemberJoinResponseDto(true, "회원가입 성공");
	}
	

}
