package com.insightcrew.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.insightcrew.domain.member.vo.MemberVo;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class MemberRepository {
	
	private final MemberMapper memberMapper;
	
	public MemberVo findByUserid(String userid) {
		return memberMapper.findByUserid(userid);
	}
	public MemberVo findByNickname(String nickname) {
		return memberMapper.findByNickname(nickname);
	}
	public List<MemberVo> findAll() {
		return memberMapper.findAll();
	}
	
	
	public void updateName(String userid, String name) {
		memberMapper.updateName(userid, name);
	}
	
	public void updateNickname(String userid, String nickname) {
		memberMapper.updateNickname(userid, nickname);
	}
	
	public void updatePassword(String userid, String encodPassword) {
		memberMapper.updatePassword(userid, encodPassword);
	}
	
	
	
	public void insertMember(MemberVo membervo) {
		memberMapper.insertMember(membervo);
	}
	
	
	//탈퇴
	public void updateStatus(String userid) {
		memberMapper.updateStatus(userid);
	}
}
