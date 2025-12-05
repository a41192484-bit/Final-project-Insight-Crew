package com.insightcrew.domain.member.dto;

import lombok.Data;

@Data
public class MemberJoinRequestDto {
	private String id;
	private String password;
	private String name;
	private String nickname;
}
