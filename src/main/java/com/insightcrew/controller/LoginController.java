package com.insightcrew.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.insightcrew.dto.LoginRequestDto;



@Controller
public class LoginController {
	
	@PostMapping("/login")
	public String requestLogin(@ModelAttribute LoginRequestDto logindto) {
		return null;
	}
	
}
