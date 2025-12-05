package com.insightcrew.repositary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.member.vo.MemberVo;


@Mapper
public interface MemberRepositary {
	void save(MemberVo vo);
	
	MemberVo findByUserid(String userid);
	MemberVo findByNickname(String nickname);
}
