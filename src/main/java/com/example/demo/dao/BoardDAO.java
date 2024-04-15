package com.example.demo.dao;


import com.example.demo.entity.Board;
import com.example.demo.entity.Users;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardDAO extends JpaRepository<Board, Integer> {
	
	//중고거래(사진형게시판) 16개씩 조회(페이징용)
    @Query(value = "SELECT * FROM board WHERE b_code = ?1 ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1",
    		nativeQuery = true)
    public Page<Board> findBoardByBCode(int b_code, Pageable pageable); //start는 임시
    
    //getNextBno(게시판 구분해서)
    @Query(value ="select ifnull(max(bno),0)+1 from board where b_code = ?", nativeQuery = true)
    public int getNextBno(int b_code);
    
    //게시판 상세 -(게시판번호, 게시글번호)
    @Query(value ="SELECT b.*, u.u_name, u.u_fname, r.r_name FROM board b INNER JOIN users u ON b.uno = u.uno INNER JOIN regioncode r ON b.rno = r.rno WHERE b.bno = ?1 AND b.b_code = ?2", nativeQuery = true)
    public Map<String, Object> detailBoard(int bno, int b_code);
    
    //특정 게시판의 전체 레코드 수 count (페이징 처리용)
    @Query(value ="select count(*) from board where b_code=?", nativeQuery = true)
    public int cntTotalRecord(int b_code);
    
    //상세페이지 들어갈 때마다 해당 게시글 조회수 1 증가
    @Modifying
    @Transactional
    @Query(value="UPDATE board SET b_hit=b_hit+1 WHERE bno = ? and b_code=?", nativeQuery = true)
    public int updateHit(int bno, int b_code);
    
    //제목으로 검색
    @Query(value="SELECT * FROM board WHERE b_code = ?1 AND b_title LIKE CONCAT('%', ?2, '%')\r\n"
    		+ "ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1 AND b_title LIKE CONCAT('%', ?2, '%')",
    		nativeQuery = true)
    public Page<Board> searchBoardByBTitle(int b_code, String search ,Pageable pageable);
    
    //지역과 제목으로 검색
    @Query(value="SELECT * FROM board WHERE b_code = ?1 AND rno = ?2 AND b_title LIKE CONCAT('%', ?3, '%') \r\n"
    		+ "ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1 AND rno = ?2 AND b_title LIKE CONCAT('%', ?3, '%')",
    		nativeQuery = true)
    public Page<Board> searchBoardByBTitleAndRegion(int b_code ,String rno, String search, Pageable pageable);
}

