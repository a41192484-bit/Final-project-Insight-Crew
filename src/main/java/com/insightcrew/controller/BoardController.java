package com.insightcrew.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.insightcrew.domain.board.dto.BoardDetailResponseDto;
import com.insightcrew.domain.board.dto.BoardListResponseDto;
import com.insightcrew.domain.board.dto.BoardRequestDto;
import com.insightcrew.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

	private final BoardService boardservice;

	// 리스트
	@GetMapping("/list")
	public String boardList(Model model) {
		List<BoardListResponseDto> list = boardservice.findAll();

		model.addAttribute("boardList", list);
		return "board/board-list";
	}

	// 작성폼
	@GetMapping("/write")
	public String Writepage() {
		return "board/board-write";
	}

	// 글작성
	@PostMapping("/write")
	public String BoardWrite(BoardRequestDto boardRequest, @RequestParam(name="files", required=false) List<MultipartFile> files) throws Exception {
		boardservice.write(boardRequest,files);
		return "redirect:/board/list";
	}

	// 상세보기
	@GetMapping("/detail/{id}")
	public String BoardDetail(@PathVariable("id") Long BoardId, Model model) {
		BoardDetailResponseDto board = boardservice.detail(BoardId);
		model.addAttribute("board", board);
		return "board/board-detail";
	}

	// 삭제
	@PostMapping("/delete")
	public String BoardDel(@RequestParam("boardId") Long BoardId) {
		boardservice.delete(BoardId);
		return "redirect:/board/list";
	}

	// 수정화면
	@GetMapping("/update/{id}")
	public String BoardUpdateForm(@PathVariable("id") Long id, Model model) {
		BoardRequestDto boardrequest = boardservice.updatedetail(id);
		model.addAttribute("boardrequest", boardrequest);
		return "board/board-update";
	}

	// 수정저장
	@PostMapping("/update")
	public String BoardUpdate(BoardRequestDto request) throws Exception {
		boardservice.update(request);
		return "redirect:/board/list";
	}

	// 검색
	@GetMapping("/search")
	public String BoardSearch(@RequestParam("keyword") String keyword, @RequestParam("type") String type, Model model) {

		List<BoardListResponseDto> search;

		switch (type) {
		case "memberId":
			search = boardservice.searchMember(keyword);
			break;
		case "content":
			search = boardservice.searchContent(keyword);
			break;
		default:
			search = boardservice.searchTitle(keyword);
			break;
		}

		model.addAttribute("boardList", search);
		model.addAttribute("keyword", keyword);
		return "board/board-list";
	}
	
}
