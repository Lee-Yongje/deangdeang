package com.example.demo.service;

import com.example.demo.dao.BoardCodeDAO;
import com.example.demo.dao.BoardDAO;
import com.example.demo.entity.Board;
import com.example.demo.entity.News;
import com.example.demo.entity.Users;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {

   @Autowired
   private BoardDAO dao;
   
   @Autowired
   private BoardCodeDAO codedao;
   
//   사진 없는 게시판 조회 시작
   
   // 게시판명으로 게시판 번호 가져오기
   public int findBCodeByBName(String b_name) {
	   return codedao.findBCodeByBName(b_name);
   }
   
   // 게시판명으로 게시판 번호 가져오기
   public String findBNameByBCode(int b_code) {
	   return codedao.findBNameByBCode(b_code);
   }
   
   // 조회
   public List<Map<String ,Object>> findByBcode(int b_code) {
	   return dao.findByBcode(b_code);
   }
   
 //게시판용 getNextNo
   public int getNextBno(int b_code) {
	   return dao.getNextBno(b_code);
   }
   
//   사진 없는 게시판 조회 끝
   
   
   //조회
   public Page<Board> listUsedgood(int b_code, Pageable pageable){
	   return dao.findBoardByBCode(b_code, pageable);
   }
   
   //제목으로 검색
   public Page<Board> searchUsedgoodByTitle(int b_code, String search, Pageable pageable){
	   return dao.searchBoardByBTitle(b_code, search, pageable);
   }
   
   //제목과 지역으로 검색
   public Page<Board> searchUsedgoodByTitleAndRegion(int b_code, String rno, String search, Pageable pageable){
	   return dao.searchBoardByBTitleAndRegion(b_code, rno, search,pageable);
   }
   
   //일단 중고장터용 getNextNo
   public int getNextUsedgoodBno() {
	   return dao.getNextBno(6);
   }
   
   //중고장터 게시글 insert
   public void insertUsedgood(Board b) {
	   dao.save(b);
   }
   
   //게시글 상세
   public Map<String,Object> detailBoard(int bno, int b_code) {
	   return dao.detailBoard(bno, b_code);
   }
   
   //특정 게시판의 전체 레코드 수 
   public int cntTotalRecord(int b_code) {
	   return dao.cntTotalRecord(b_code);
   }
   
   //조회수 1 증가
   public void updateHit(int bno, int b_code) {
	   dao.updateHit(bno, b_code);
   }
   
   
}