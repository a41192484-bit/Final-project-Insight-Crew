package com.insightcrew.repositary;

import org.apache.ibatis.annotations.Mapper;

import com.insightcrew.domain.member.vo.MemberVo;


@Mapper
public interface MemberRepositary {
	Long save(MemberVo vo);
	
	//아이디로 회원 조회
	MemberVo findByUserid(String userid);
	
	//닉네임 중복 조회
	MemberVo findByNickname(String nickname);

	//회원저장
	int insertMamber(MemberVo vo);
}
