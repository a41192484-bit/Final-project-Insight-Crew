package com.insightcrew.domain.member.vo;

import java.time.LocalDateTime;

import com.insightcrew.domain.member.enums.UserRole;
import com.insightcrew.domain.member.enums.UserStatus;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberVo {
	// pk id(고유식별자)는 세션, 인증, 권한 처리할 때 필수
	private Long pkId;
	
	private String userid;
	private String password;
	private String name;
	private String nickname;
	
	private UserRole role;
	private UserStatus status;
	
	private LocalDateTime indate;
	private LocalDateTime outdate;
}
