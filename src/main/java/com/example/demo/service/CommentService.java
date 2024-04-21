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
	
	// 댓글 List
	public List<Map<String, Object>> List(int b_code, int bno){
		return dao.listComment(b_code, bno);
	}
}
