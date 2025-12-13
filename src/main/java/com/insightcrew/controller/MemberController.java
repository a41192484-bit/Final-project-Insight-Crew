package com.insightcrew.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.insightcrew.domain.auth.vo.CustomUserDetails;
import com.insightcrew.domain.member.dto.MemberInfoDto;
import com.insightcrew.service.MemberService;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberservice;

	
	
	//회원정보 조회, 뷰
	@GetMapping("/info")
	public String memberinfo(Model model, @AuthenticationPrincipal CustomUserDetails loginuser) {
		//db에서 정보 조회
		MemberInfoDto loginuserinfo = memberservice.findUser(loginuser.getUsername());
		
		//비밀번호는 마스킹해서 넘김
		loginuserinfo.setPassword("***");
		
		//커스텀 유저 디테일이 이미 유저의 모든 정보를 가지고 있음.
		//loginuserinfo라는 자바 객체를 타임리프에서 사용할 때 "loginuser"라는 이름으로 접근.
		model.addAttribute("loginuser", loginuserinfo);
		
		return "member/member-info";
	}
	
	
	
	
	//회원정보 수정-이름
	@PostMapping("/update/name")
	@ResponseBody
	public String updateName(@AuthenticationPrincipal CustomUserDetails loginuser,
							@RequestBody Map<String, String> body) {
		String newName=body.get("name");
		memberservice.updateName(loginuser.getUsername(), newName);
		return "ok";
	}
	
	//회원정보 수정-닉네임
	@PostMapping("/update/nickname")
	@ResponseBody
		public String updateNickname(@AuthenticationPrincipal CustomUserDetails loginuser,
									@RequestBody Map<String, String> body) {
			String newNickname = body.get("nickname");
			try {
				memberservice.updateNickname(loginuser.getUsername(), newNickname);
				return "ok";
			}catch(IllegalArgumentException e) {
				return "DUPLICATE";
			}

		}
		
		//회원정보 수정-비밀번호
	@PostMapping("/update/password")
	@ResponseBody
		public String updatePassword(@AuthenticationPrincipal CustomUserDetails loginuser,
										@RequestBody Map<String, String> body) {
			String newPassword = body.get("password");
			memberservice.updatePassword(loginuser.getUsername(), newPassword);
			return "OK";
		}
	
	
	
//	@PostMapping("/update")
//	public ResponseEntity<?> updateinfo(@AuthenticationPrincipal CustomUserDetails user,
//							@RequestBody Map<String, String> body) {
//		
//		String field = body.get("field");
//        String value = body.get("value");
//
//        try {
//        	memberservice.updateOneField(user.getUsername(), field, value);
//        	return ResponseEntity.ok("success");
//        }catch(IllegalArgumentException e) {
//        	return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
	

	
	//회원탈퇴
	@PostMapping("/inactive")
	public String inactive(@AuthenticationPrincipal CustomUserDetails loginuser, Model model) {
		System.out.println("회원탈퇴 컨트롤러 입니당");
		try {
			memberservice.inactive(loginuser.getUsername());
			//탈퇴 후 로그아웃 처리.
			SecurityContextHolder.clearContext();
			return "redirect:/auth/view";  // 탈퇴 처리 성공 메시지
		}catch(IllegalStateException e) {
			model.addAttribute("error", e.getMessage());
			return "member/member-mypage";
		}
		
	}
	
}
