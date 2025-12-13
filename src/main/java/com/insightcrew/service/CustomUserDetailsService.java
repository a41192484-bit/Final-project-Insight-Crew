package com.insightcrew.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.insightcrew.domain.auth.vo.CustomUserDetails;
import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.repository.MemberMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final MemberMapper membermapper;
	
	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
		//db에서 vo 조회
		MemberVo membervo = membermapper.findByUserid(userid);
		
		//유저 없음
		if(membervo == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		
		//디버깅용
		System.out.println("여기는 커스텀 유저 디테일 서비스");
		System.out.println(membervo.getUserid()+membervo.getRole().name());
		
		//구현 객체로 반환
		return new CustomUserDetails(membervo);
	}
}
