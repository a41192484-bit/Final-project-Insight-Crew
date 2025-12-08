package com.insightcrew.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.insightcrew.config.CustomUserDetails;
import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.repositary.MemberRepositary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final MemberRepositary memberRepositary;
	
	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
		MemberVo membervo = memberRepositary.findByUserid(userid);
		
		if (membervo == null) {
			throw new UsernameNotFoundException(userid+" 라는 사용자 없음.");
		}
		return new CustomUserDetails(membervo);
	}
}
