package com.insightcrew.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginRequestDto {
	private String userid;
	private String password;
}
