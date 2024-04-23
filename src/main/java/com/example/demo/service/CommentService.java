package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CommentDAO;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;

@Service
public class CommentService {

	@Autowired
	private CommentDAO dao;
	
	// 댓글테이블 번호 증가
	public int getNextCno() {
		return dao.getNextCno();
	}
	
	// 댓글 Insert
	public void insert(Comment c) {
		dao.save(c);
	}
	
	//댓글 update
	public void update(Comment c) {
		dao.save(c);
	}
	
	// 댓글 List
	public List<Map<String, Object>> List(int b_code, int bno){
		return dao.listComment(b_code, bno);
	}
	
	//(수정을 위한) 원본 댓글 가져오기
	public Comment getOldComment(int cno){
		return dao.findById(cno).get();
	}
	
	//댓글 삭제
	public void delete(Comment c) {
		dao.delete(c);
	}
}
