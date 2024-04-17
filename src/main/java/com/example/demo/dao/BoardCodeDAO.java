package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.BoardCode;

@Repository
public interface BoardCodeDAO extends JpaRepository<BoardCode, Integer> {

	@Query(value = "select b_name from boardcode where b_code=?", nativeQuery = true)
	public String findById(int boardCode);

	
	// 게시판 명으로 게시판 번호 가져오기
	@Query(value="select b_code from boardcode where b_name=?1", nativeQuery = true)
	public int findBCodeByBName(String b_name);
	
	// 게시판 번호로 게시판 명 가져오기
	@Query(value="select b_name from boardcode where b_code=?1", nativeQuery = true)
	public String findBNameByBCode(int b_code);
}
