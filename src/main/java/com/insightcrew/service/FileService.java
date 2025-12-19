package com.insightcrew.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.insightcrew.domain.board.dto.FileDto;
import com.insightcrew.repository.BoardFileMapper;

import lombok.RequiredArgsConstructor;

@Service // 스프링의 서비스 레이어
@RequiredArgsConstructor // final 필드를 자동 DI
public class FileService {
	private final BoardFileMapper boardfilemapper;
	// Mapper 주입받기 (DB 작업 담당)

	// 실제 파일 저장할 폴더 경로
	private final String uploadDir = "C:/upload/";

	/**
	 * ========================= 파일 업로드 로직 =========================
	 **/
	public void uploadFile(MultipartFile file, Long boardId) throws Exception {
	    if(file.isEmpty()) return;

	    File folder = new File(uploadDir);
	    if(!folder.exists()) folder.mkdirs();

	    String originalName = file.getOriginalFilename();
	    String savedName = UUID.randomUUID() + "_" + originalName;
	    String filePath = uploadDir + savedName;
	    file.transferTo(new File(filePath));

	    FileDto dto = new FileDto();
	    dto.setBoardId(boardId); // 게시글 ID 추가
	    dto.setOriginalName(originalName);
	    dto.setSavedName(savedName);
	    dto.setFilePath(filePath);
	    dto.setFileSize(file.getSize());

	    boardfilemapper.insertFile(dto);
	}
	// 새로 추가 (boardId 없이 파일만 업로드 가능)
	public void uploadFile(MultipartFile file) throws Exception {
	    uploadFile(file, null);
	}

	/**
	 * ========================= 파일 다운로드 =========================
	 **/
	public Resource downloadFile(Long id) throws Exception {

		// DB에서 파일 정보 조회
		FileDto file = boardfilemapper.findById(id);

		// 파일 경로 생성
		Path path = Paths.get(file.getFilePath());

		// 스프링이 제공하는 Resource 객체로 파일 로드
		Resource resource = new UrlResource(path.toUri());

		// 파일 없으면 예외 발생
		if (!resource.exists()) {
			throw new FileNotFoundException("파일 없음");
		}
		return resource;
	}

	/**
	 * ========================= 파일 삭제 =========================
	 **/
	public void deleteFile(Long id) {

		// DB에서 파일 정보 조회
		FileDto file = boardfilemapper.findById(id);

		// 실제 파일 삭제
		File f = new File(file.getFilePath());
		if (f.exists())
			f.delete();

		// DB 정보 삭제
		boardfilemapper.deleteFile(id);
	}

	/**
	 * ========================= 전체 파일 목록 조회 =========================
	 **/
	public List<FileDto> getFileList() {
		return boardfilemapper.findAll();
	}
}
