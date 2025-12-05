package com.insightcrew.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insightcrew.domain.member.dto.MemberJoinRequestDto;
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
	
	//가입
	public void join(MemberJoinRequestDto dto) {
		if(memberRepositary.findByUserid(dto.getId()) != null) {
			throw new IllegalArgumentException("이미 존재하는 아이디");
		}
		
		String encodedPw = passwordEncoder.encode(dto.getPassword());

		MemberVo vo = MemberVo.builder()
				.userid(dto.getUserid())
				.password(encodedPw)
				.name(dto.getName())
                .nickname(dto.getNickname())
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .indate(LocalDateTime.now())
                .build();
		
		memberRepository.save(vo);
	}
	
	public boolean existsByUserid(String userid) {
		return memberRepository.findByUserid(userid) != null;
	}
	
	public boolean existsByNickname(String nickname) {
        return memberRepository.findByNickname(nickname) != null;
    }

}
