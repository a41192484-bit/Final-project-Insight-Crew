package com.insightcrew.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insightcrew.domain.member.dto.MemberJoinRequestDto;
import com.insightcrew.domain.member.dto.MemberJoinResponseDto;
import com.insightcrew.domain.member.enums.UserRole;
import com.insightcrew.domain.member.enums.UserStatus;
import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	//아이디 조회
	public MemberVo findUser(String userid) {
		return memberRepository.findByUserid(userid);
	}
	
	
	//회원가입
	@Transactional
	public MemberJoinResponseDto join(MemberJoinRequestDto requestdto) {
		//서버에서 아이디 중복 체크
		if(requestdto.getUserid() == null || requestdto.getUserid().isEmpty()) {
			return new MemberJoinResponseDto(false, "아이디는 필수로 입력해주세요.");
		}
		if(existsByUserid(requestdto.getUserid())) {
			return new MemberJoinResponseDto(false, "이미 사용 중인 아이디입니다.");
			//응답dto에서 "성공여부"를 묻는 거라. 중복여부 true를 리턴받으면 중복이라 실패.
			//그래서 여기서는 false로 응답.	
		}
		
		//서버에서 닉네임 중복 체크
		if(requestdto.getNickname() != null && !requestdto.getNickname().isEmpty()){
			if(existsByNickname(requestdto.getNickname())) {
				return new MemberJoinResponseDto(false, "이미 사용 중인 닉네임입니다.");
			}
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
		
		memberRepository.insertMember(vo);
		
		return new MemberJoinResponseDto(true, "회원가입 성공");
	}
	
	//아이디 중복체크
	public boolean existsByUserid(String userid) {
		//db에서 id를 조회했는데 null이 아니면 존재=중복임.
		return memberRepository.findByUserid(userid) != null;
	}
	
	//닉네임 중복체크
	public boolean existsByNickname(String nickname) {
        return memberRepository.findByNickname(nickname) != null;
    }

	@Transactional
	public void updateOneField(String userid, String field, String value) {

	    switch (field) {
	        case "password" -> memberRepository.updatePassword(userid, value);
	        case "username" -> memberRepository.updateName(userid, value);
	        case "nickname" -> memberRepository.updateNickname(userid, value);
	    }
	}

}
