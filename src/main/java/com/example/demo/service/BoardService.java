package com.example.demo.service;

import com.example.demo.dao.BoardCodeDAO;
import com.example.demo.dao.BoardDAO;
import com.example.demo.dao.UsersDAO;
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
   
   @Autowired
   private UsersDAO udao;
   
//   사진 없는 게시판 조회 시작
   
   // 회원명으로 회원번호 가져오기 
   public Long findByUName(String u_name) {
	   return udao.findByUName(u_name);
   }
   
   // 게시판명으로 게시판 번호 가져오기
   public int findBCodeByBName(String b_name) {
	   return codedao.findBCodeByBName(b_name);
   }
   
   // 게시판명으로 게시판 번호 가져오기
   public String findBNameByBCode(int b_code) {
	   return codedao.findBNameByBCode(b_code);
   }
   
   // 자유, 질문 게시판 조회
   public List<Map<String ,Object>> findByBcode(int b_code) {
	   return dao.findByBcode(b_code);
   }
   
   // 모임 게시판 조회
   public List<Map<String ,Object>> findClubByBcode(int b_code) {
	   return dao.findClubByBcode(b_code);
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
   
   //지역으로만 검색
   public Page<Board> searchBoardByRegion(int b_code, String rno, Pageable pageable){
	   return dao.searchBoardByRegion(b_code, rno, pageable);
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
   
   //게시판 글 삭제 (기본 delete를 안 쓴 이유: 반환값이 void라서)
   public int deleteBoard( int b_code, int bno) {
	   return dao.deleteBoard(b_code, bno);
	   
   }
   
   //게시판 글 수정
   public Board update(Board b) {
	   return dao.save(b);
   }
   
   //bno랑 b_code로 게시글 Board 찾기
   public Board findBoardByBnoAndBCode(int b_code, int bno) {
	   return dao.findBoardByBnoAndBCode(b_code, bno);
   }
   
   //중고장터 판매완료시키기
   public void usedgoodSold(int b_code, int bno){
	   dao.usedgoodSold(b_code, bno);
   }   
}