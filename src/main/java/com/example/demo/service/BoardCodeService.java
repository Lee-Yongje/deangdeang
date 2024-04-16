package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BoardCodeDAO;

@Service
public class BoardCodeService {

	@Autowired
	private BoardCodeDAO dao;
	
	public String findById(int boardCode) {
		 String b_name = dao.findById(boardCode);
		 return b_name;
	}
}
