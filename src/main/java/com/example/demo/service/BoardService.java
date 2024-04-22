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
import java.util.HashMap;
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
   public Long findByUNickName(String u_name) {
	   return udao.findByUNickName(u_name);
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
   public Page<List<Map<String, Object>>> findByBcode( HashMap<String, String> map ,int b_code, Pageable pageable) {
	   String cname = map.get("cname");
	   String keyword = map.get("keyword");
	   Page<List<Map<String, Object>>> list = null;
	   if( keyword != null && !keyword.equals("")) {
		   switch(cname) {
		   case "b_title": list = dao.searchByBTitle(b_code, keyword, pageable);break;
		   case "u_nickname": list = dao.searchByUNickname(b_code, keyword, pageable);break;
		   }
	   }else {
		   list = dao.findByBcode(b_code, pageable);		   
	   }
	   return list;
   }
   
   // 모임 게시판 조회
   public Page<List<Map<String, Object>>> findClubByBcode(HashMap<String, String> map, int b_code, Pageable pageable) {
	   String rno = map.get("rno");
	   String cname = map.get("cname");
	   String keyword = map.get("keyword");
	   System.out.println("서비스 rno : " + rno);
	   System.out.println("서비스 cname : " + cname);
	   System.out.println("서비스 keyword : " + keyword);
	   Page<List<Map<String, Object>>> list = null;
	   if( rno != null && !rno.equals("rno")) {
		   // 지역 설정 O, 검색어 O
		   if(keyword != null && !keyword.equals("")) {
			   switch(cname) {
			   case "b_title": list = dao.searchByRnoAndBTitle(b_code, rno, keyword,pageable);break;
			   case "u_nickname": list = dao.searchByRnoAndUNickname(b_code,rno, keyword,pageable);break;
			   }
		   }else {
			// 지역 설정 O, 검색어 X
			   list = dao.searchByRno(b_code, rno, pageable);
		   }
	   }else {
		   // 지역 설정 X, 검색어 O
		   if( keyword != null && !keyword.equals("")) {
			   switch(cname) {
			   case "b_title": list = dao.searchClubByBTitle(b_code, keyword, pageable);break;
			   case "u_nickname": list = dao.searchClubByUNickname(b_code, keyword, pageable);break;
			   }
		   }else {
			   // 지역 설정 X, 검색어 X
			   // 모임게시판 기본 조회
			   list = dao.findClubByBcode(b_code, pageable);		   
		   }		   
	   }
	   return list;
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
   
   //진행중인 것만 조회
   public Page<Board> listOngoing(int b_code, Pageable pageable){
	   return dao.findBoardByBCodeOngoing(b_code, pageable);
   }
   
   //제목으로 검색
   public Page<Board> searchUsedgoodByTitle(int b_code, String search, String ongoing, Pageable pageable){
	   if(ongoing!=null && ongoing.equals("1")) {
		   return dao.searchBoardByBTitleOngoing(b_code, search, pageable);
	   }
	   return dao.searchBoardByBTitle(b_code, search, pageable);
   }
   
   //제목과 지역으로 검색
   public Page<Board> searchUsedgoodByTitleAndRegion(int b_code, String rno, String search, String ongoing, Pageable pageable){
	   if(ongoing!=null && ongoing.equals("1")) {
		   return dao.searchBoardByBTitleAndRegionOngoing(b_code, rno, search, pageable);
	   }
	   return dao.searchBoardByBTitleAndRegion(b_code, rno, search,pageable);
   }
   
   //지역으로만 검색
   public Page<Board> searchBoardByRegion(int b_code, String rno, Pageable pageable){
	   return dao.searchBoardByRegion(b_code, rno, pageable);
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
   
   //중고장터 판매완료, 모임게시판 모임완료시키기
   public void usedgoodSold(int b_code, int bno){
	   dao.usedgoodSold(b_code, bno);
   }
   
   //내 글 보기(마이페이지)
   public Page<Board> findByUno(Long uno, Pageable pageable){
	   return dao.findByUno(uno,pageable);
   }
}