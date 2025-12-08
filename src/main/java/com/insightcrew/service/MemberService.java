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
	
	
	//회원가입
	public MemberJoinResponseDto join(MemberJoinRequestDto requestdto) {
		//서버에서 아이디 중복 체크 
		if(existsByUserid(requestdto.getUserid())) {
			//응답dto에서 "성공여부"를 묻는 거라. 중복여부 true를 리턴받으면 중복이라 실패.
			//그래서 여기서는 false로 응답.
			return new MemberJoinResponseDto(false, "이미 사용 중인 아이디입니다.");
		}
		
		//서버에서 닉네임 중복 체크
		if(existsByNickname(requestdto.getNickname())) {
			return new MemberJoinResponseDto(false, "이미 사용 중인 닉네임입니다.");
		}
		
		//비밀번호 암호화. 감싸기.
		//스프링 시큐리티에 있는 BCryptPasswordEncoder 암호화 사용.
		String encodedPw = passwordEncoder.encode(requestdto.getPassword());
		
		//memberVO 새로운 정보를 담을 객체 생성
		MemberVo vo = new MemberVo();
		//클라이언트가 보낸 값 저장
		vo.setUserid(requestdto.getUserid());
		vo.setPassword(encodedPw);
		vo.setName(requestdto.getName());
		vo.setNickname(requestdto.getNickname());
		vo.setRole(UserRole.USER);
		vo.setStatus(UserStatus.ACTIVE);
		
		memberRepositary.insertMember(vo);
		
		return new MemberJoinResponseDto(true, "회원가입 성공");
	}
	
	//아이디 중복체크
	public boolean existsByUserid(String userid) {
		//db에서 id를 조회했는데 null이 아니면 존재=중복임.
		return memberRepositary.findByUserid(userid) != null;
	}
	
	//닉네임 중복체크
	public boolean existsByNickname(String nickname) {
        return memberRepositary.findByNickname(nickname) != null;
    }

}
