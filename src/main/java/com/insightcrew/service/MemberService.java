package com.insightcrew.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insightcrew.domain.auth.dto.MemberJoinRequestDto;
import com.insightcrew.domain.auth.dto.MemberJoinResponseDto;
import com.insightcrew.domain.member.dto.MemberInfoDto;
import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final MappingService mappingService;
	
	
	//회원 조회
	public MemberInfoDto findUser(String userid) {
		MemberVo vo = memberRepository.findByUserid(userid);
		return mappingService.toDto(vo);
	}
	public void updateName(String userid, String name) {
		memberRepository.updateName(userid, name);
	}
	public void updateNickname(String userid, String nickname) {
		memberRepository.updateNickname(userid, nickname);
	}
	public void updatePassword(String userid, String password) {
		String encod = passwordEncoder.encode(password);
		memberRepository.updatePassword(userid, encod);
	}
	
	
	
	
	//회원가입
	public MemberJoinResponseDto join(MemberJoinRequestDto requestdto) {
		
		//기본값 검증
		if(requestdto.getUserid() == null || requestdto.getUserid().trim().isEmpty()) {
			return new MemberJoinResponseDto(false, "아이디는 필수로 입력해주세요.");
		}
		if(requestdto.getPassword() == null || requestdto.getPassword().trim().isEmpty()) {
			return new MemberJoinResponseDto(false, "비밀번호는 필수로 입력해주세요.");
		}
		
		
		//중복체크
		if(existsByUserid(requestdto.getUserid())) {
			return new MemberJoinResponseDto(false, "이미 사용 중인 아이디입니다.");
			//여기서 true는 "성공여부"를 묻는 것. 중복 아이디는 성공이 아니고 실패.
			//그래서 여기서는 false로 응답.	
		}
		if(requestdto.getNickname() != null && !requestdto.getNickname().isEmpty()){
			if(existsByNickname(requestdto.getNickname())) {
				return new MemberJoinResponseDto(false, "이미 사용 중인 닉네임입니다.");
			}
		}
		
		
		//비밀번호 암호화. 감싸기.
		//스프링 시큐리티에 있는 BCryptPasswordEncoder 암호화 사용.
		String encodedPassword = passwordEncoder.encode(requestdto.getPassword());
		
		//memberVO 새로운 정보를 담을 객체 생성
		MemberVo vo = mappingService.toMemberVo(requestdto, encodedPassword);
		
		//회원정보 저장
		memberRepository.insertMember(vo);
		
		return new MemberJoinResponseDto(true, "회원가입 성공");
	}
	
	//아이디 중복체크
	public boolean existsByUserid(String userid) {
		//db에서 id를 조회했는데 null이 아니면 존재=중복임.
		//true 중복, false 중복아님
		return memberRepository.findByUserid(userid) != null;
	}
	
	//닉네임 중복체크
	public boolean existsByNickname(String nickname) {
		//트루=중복 , false=중복아님
        return memberRepository.findByNickname(nickname) != null;
    }


	
	
	
	
	
	//공용 업데이트(수정)
	public void updateOneField(String userid, String field, String value) {
		switch(field) {
		case "name":
			memberRepository.updateName(userid, value);
			break;
		case "nickname":
			if(existsByNickname(value)) {
				throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
			}
			memberRepository.updateNickname(userid, value);
			break;
		case "password":
			String encoded = passwordEncoder.encode(value);
			memberRepository.updatePassword(userid, encoded);
			break;
			
		default:
			throw new IllegalArgumentException("허용되지 않은 필드입니다: "+field);
		}
	}

	
	
	// 패스워드 암호화
	public void updateEncodePassword(String userid, String Password) {
		
		
	}
	
	
	
	
	
	//회원탈퇴
	@Transactional
	public void inactive(String userid) {
		
		int result = memberRepository.updateStatus(userid);
		
		//result값: 1=정상적 탈퇴 처리, 0=해당 유저 아이디 없거나 이미 inactive 상태
		if(result != 1) {
			throw new IllegalStateException("회원 탈퇴 실패");
		}
		
		System.out.println("회원탈퇴 서비스 입니당");
    }
	
	
}
