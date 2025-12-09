package com.insightcrew.repository;

import org.apache.ibatis.annotations.Mapper;

import com.insightcrew.domain.member.vo.MemberVo;


@Mapper
public interface MemberRepository {
	
	//아이디 조회
	MemberVo findByUserid(String userid);
	
	//닉네임 조회
	MemberVo findByNickname(String nickname);
	
	//회원가입 insert
	//반환값이 필요 없기 때문에 void 사용
	void insertMember(MemberVo membervo);
}
