package com.insightcrew.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {
//수정용 dto
	
	private String name;
	private String nickname;
	private String password;
	
}
