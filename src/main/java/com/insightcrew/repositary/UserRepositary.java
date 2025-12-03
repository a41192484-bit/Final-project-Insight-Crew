package com.insightcrew.repositary;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepositary {
	// dao. db에서 데이터를 가져오거나 db에 넣는 역할.
	
	// 로그인 요청
	UserEntity findByuserid() {
		return null;
	}
}
