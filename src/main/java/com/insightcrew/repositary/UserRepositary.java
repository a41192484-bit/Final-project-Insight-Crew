package com.insightcrew.repositary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.member.dto.LoginRequestDto;
import com.insightcrew.domain.member.vo.UserEntity;

@Mapper
public interface UserRepositary {
	// dao. db에서 데이터를 가져오거나 db에 넣는 역할.
	
	// 아이디 찾기
	UserEntity findByUserid(@Param("userid") LoginRequestDto userid);
	
	// 비번찾기
	UserEntity findBypassword(@Param("password") LoginRequestDto password);
}
