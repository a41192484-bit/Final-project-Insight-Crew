package com.insightcrew.service;

import org.springframework.stereotype.Service;

import com.insightcrew.domain.auth.dto.LoginRequestDto;
import com.insightcrew.domain.member.vo.MemberVo;
import com.insightcrew.repositary.MemberRepositary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepositary memberRepositary;
	
	public MemberVo findUser(LoginRequestDto userid) {
		return memberRepositary.findByUserid(userid);
	}
}
