package com.insightcrew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insightcrew.dto.LoginRequestDto;

@Service
public class UserService {
	// 아이디가 db에 있는지 확인
	@Autowired
	public void String findByUserid(LoginRequestDto logindto);
	// 아이디 있으면 비번도 비교
	// 모두 일치하면 로그인 응답dto에 값을 넣어서 컨트롤러로 넘김
}
