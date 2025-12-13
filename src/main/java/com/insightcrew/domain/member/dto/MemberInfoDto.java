package com.insightcrew.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoDto {
	//뷰 전용
	private String userid;
	private String password;
	private String name;
	private String nickname;
}
