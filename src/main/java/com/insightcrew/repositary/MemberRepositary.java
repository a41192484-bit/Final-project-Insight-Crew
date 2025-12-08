package com.insightcrew.repositary;

import org.apache.ibatis.annotations.Mapper;

import com.insightcrew.domain.member.vo.MemberVo;


@Mapper
public interface MemberRepositary {
	//아이디로 회원 조회
	MemberVo findByUserid(String userid);
	
	//닉네임 중복 체크
	MemberVo findByNickname(String nickname);
	
	//아이디 중복체크

	//회원가입
	int insertMember(MemberVo vo);
}
