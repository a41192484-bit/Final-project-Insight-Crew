package com.insightcrew.service;

import org.springframework.stereotype.Component;

import com.insightcrew.domain.auth.dto.MemberJoinRequestDto;
import com.insightcrew.domain.member.dto.MemberInfoDto;
import com.insightcrew.domain.member.enums.UserRole;
import com.insightcrew.domain.member.enums.UserStatus;
import com.insightcrew.domain.member.vo.MemberVo;


@Component
public class MappingService {
	
	//dto <---> vo 변환 담당
	public MemberInfoDto toDto(MemberVo vo) {
		if(vo == null) return null;
		
		MemberInfoDto dto = new MemberInfoDto();
		
		dto.setUserid(vo.getUserid());
		dto.setPassword(vo.getPassword());
		dto.setName(vo.getName());
		dto.setNickname(vo.getNickname());
		
		return dto;
	}
	public MemberVo toVo(MemberInfoDto dto, String encodedPassword) {
		if(dto == null) return null;
		
		MemberVo vo = new MemberVo();
		
		vo.setUserid(dto.getUserid());
		vo.setPassword(encodedPassword);
		vo.setName(dto.getName());
		vo.setNickname(dto.getNickname());
		vo.setRole(UserRole.USER);
		vo.setStatus(UserStatus.ACTIVE);
		
		return vo;
	}
	
	
	//회원 가입용
	public MemberVo toMemberVo(MemberJoinRequestDto dto, String encodedPassword) {
		MemberVo vo = new MemberVo();
		
		vo.setUserid(dto.getUserid());
		vo.setPassword(encodedPassword);
		vo.setName(dto.getName());
		vo.setNickname(dto.getNickname());
		vo.setRole(UserRole.USER);
		vo.setStatus(UserStatus.ACTIVE);
		
		return vo;
	}
	
}
