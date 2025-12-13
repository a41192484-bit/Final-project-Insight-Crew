package com.insightcrew.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightcrew.domain.member.vo.MemberVo;

@Mapper
public interface MemberMapper {

	//아이디 조회 - 한명
	MemberVo findByUserid(String userid);
	
	//닉네임 조회
	MemberVo findByNickname(String nickname);
	
	
	//회원가입 insert
	//반환값이 필요 없기 때문에 void 사용
	void insertMember(MemberVo membervo);
	
	
	
	//수정
	void updateName(@Param("userid") String userid, @Param("name") String name);
    void updateNickname(@Param("userid") String userid, @Param("nickname") String nickname);
    void updatePassword(@Param("userid") String userid, @Param("password") String password);
    
    
    
    //전체 정보 조회
	List<MemberVo> findAll();

	
	
	//회원탈퇴 - 상태 업데이트
	int updateStatus(@Param("userid") String userid);
	
}
