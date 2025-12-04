package com.insightcrew.repositary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.auth.dto.LoginRequestDto;
import com.insightcrew.domain.member.vo.MemberVo;

@Mapper
public interface MemberRepositary {
	// dao. db에서 데이터를 가져오거나 db에 넣는 역할.
	
	// 아이디 찾기
	MemberVo findByUserid(@Param("userid") LoginRequestDto userid);
	
	// 비번찾기
	MemberVo findBypassword(@Param("password") LoginRequestDto password);
}
