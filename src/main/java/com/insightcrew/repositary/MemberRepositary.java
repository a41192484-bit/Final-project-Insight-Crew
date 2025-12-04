package com.insightcrew.repositary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.member.vo.MemberVo;


@Mapper
public interface MemberRepositary {
	// 아이디 찾기
	MemberVo findByUserid(@Param("userid") String userid);
	
	// 비번찾기
	MemberVo findBypassword(@Param("password") String password);
}
