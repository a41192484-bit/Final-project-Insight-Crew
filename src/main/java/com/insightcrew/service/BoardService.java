package com.insightcrew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.insightcrew.domain.board.dto.BoardDetailResponse;
import com.insightcrew.domain.board.dto.BoardListResponse;
import com.insightcrew.domain.board.dto.BoardRequest;
import com.insightcrew.repository.BoardMapper;

@Service
public class BoardService {

    @Autowired
    BoardMapper boardmapper;
    @Autowired
    private FileService fileService;
    
    @Transactional
    public void write(BoardRequest boardRequest, List<MultipartFile> files) throws Exception {
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
    public void write(BoardRequest boardRequest) throws Exception {
        write(boardRequest, null);
    }
    public BoardDetailResponse detail(Long BoardId) {
        return boardmapper.detail(BoardId);
    }

    @Transactional
    public void delete(Long BoardId) {
        boardmapper.delete(BoardId);
    }

    public List<BoardListResponse> findAll() {
    	 List<BoardListResponse> list = boardmapper.findAll();
    	    ViewOrder(list);
    	    return list;
    }

    public BoardRequest updatedetail(Long id) {
        return boardmapper.findById(id);
    }

    public void update(BoardRequest request) {
        boardmapper.update(request);
    }

    public List<BoardListResponse> searchTitle(String keyword) {
        List<BoardListResponse> list = boardmapper.findTitle(keyword);
        ViewOrder(list);
        return list;
    }

    public List<BoardListResponse> searchMember(String keyword) {
        List<BoardListResponse> list = boardmapper.findMember(keyword);
        ViewOrder(list);
        return list;
    }

    public List<BoardListResponse> searchContent(String keyword) {
        List<BoardListResponse> list = boardmapper.findContent(keyword);
        ViewOrder(list);
        return list;
    }

    private void ViewOrder(List<BoardListResponse> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setViewOrder(i + 1);
        }
    }

}
