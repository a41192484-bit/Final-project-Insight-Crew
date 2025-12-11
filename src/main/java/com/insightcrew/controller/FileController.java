package com.insightcrew.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.insightcrew.domain.board.dto.FileDto;
import com.insightcrew.service.FileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;
	//파일업로드
	@PostMapping("/file/upload")
	public String upload(@RequestParam("files") MultipartFile file) throws Exception {
		fileService.uploadFile(file); //서비스에게 파일 저장 맡김
		return "redirect:/file/list"; //업로드 후 목록 페이지로 이동
	}
	
	//파일리스트페이지
	@GetMapping("/file/list")
	public String list(Model model) {
		model.addAttribute("files", fileService.getFileList());
		
		return "file/list"; //타임리프 파일
	}
	//파일다운로드
	@GetMapping("/file/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable Long id) throws Exception {
		Resource file = fileService.downloadFile(id); //서비스로부터 파일 리소스 가져옴
		//다운로드 시 원본 파일명으로 다운받도록 헤더 설정
		FileDto dto = fileService.getFileList()
								.stream()
								.filter(f -> f.getId().equals(id))
								.findFirst()
								.orElse(null);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + dto.getOriginalName() + "\"")
				.body(file);
	}
	//파일삭제
	@GetMapping("/file/delete/{id}")
	public String delete(@PathVariable Long id) {
		fileService.deleteFile(id); //서비스에 삭제시킴
		return "redirect:/file/list"; //목록으로 리다이렉트
	}
}
