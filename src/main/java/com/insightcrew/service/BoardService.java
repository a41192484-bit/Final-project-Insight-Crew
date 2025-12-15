package com.insightcrew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.insightcrew.domain.board.dto.BoardDetailResponseDto;
import com.insightcrew.domain.board.dto.BoardListResponseDto;
import com.insightcrew.domain.board.dto.BoardRequestDto;
import com.insightcrew.repository.BoardMapper;

@Service
public class BoardService {

    @Autowired
    BoardMapper boardmapper;
    @Autowired
    private FileService fileService;
    
    @Transactional
    public void write(BoardRequestDto boardRequest, List<MultipartFile> files) throws Exception {
        boardmapper.write(boardRequest); // 게시글 먼저 저장
        Long boardId = boardRequest.getBoardId(); // insert 후 생성된 PK 가져오기

        if(files != null && !files.isEmpty()) {
            for(MultipartFile file : files) {
                fileService.uploadFile(file, boardId); // boardId 추가해서 파일 저장
            }
        }
    }
 // 파일 없는 경우 오버로딩
    @Transactional
    public void write(BoardRequestDto boardRequest) throws Exception {
        write(boardRequest, null);
    }
    public BoardDetailResponseDto detail(Long BoardId) {
        return boardmapper.detail(BoardId);
    }

    @Transactional
    public void delete(Long BoardId) {
        boardmapper.delete(BoardId);
    }

    public List<BoardListResponseDto> findAll() {
    	 List<BoardListResponseDto> list = boardmapper.findAll();
    	    ViewOrder(list);
    	    return list;
    }

    public BoardRequestDto updatedetail(Long id) {
        return boardmapper.findById(id);
    }

    public void update(BoardRequestDto request) {
        boardmapper.update(request);
    }

    public List<BoardListResponseDto> searchTitle(String keyword) {
        List<BoardListResponseDto> list = boardmapper.findTitle(keyword);
        ViewOrder(list);
        return list;
    }

    public List<BoardListResponseDto> searchMember(String keyword) {
        List<BoardListResponseDto> list = boardmapper.findMember(keyword);
        ViewOrder(list);
        return list;
    }

    public List<BoardListResponseDto> searchContent(String keyword) {
        List<BoardListResponseDto> list = boardmapper.findContent(keyword);
        ViewOrder(list);
        return list;
    }

    private void ViewOrder(List<BoardListResponseDto> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setViewOrder(i + 1);
        }
    }

}
