package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.insightcrew.domain.auth.dto.LoginRequestDto;



@Controller
@RequestMapping("/auth")
public class LoginController {
	
	@PostMapping("/login")
	public String requestLogin(@ModelAttribute LoginRequestDto logindto) {
		return null;
	}
	
}
