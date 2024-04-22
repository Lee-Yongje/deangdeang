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
	
//	사진 없는 게시판 메소드 시작
	
	// 자유, 질문 게시판 조회
	@Query(value = "SELECT b.*, u_nickname FROM board b "
			+ "INNER JOIN users u ON b.uno = u.uno "
			+ "WHERE b_code = ?1 order by b_date desc",
			countQuery = "select count(*) from board where b_code=?1",
			nativeQuery = true)
	public Page<List<Map<String, Object>>> findByBcode(int b_code, Pageable pageable);
	
	// 자유, 질문 게시판 제목으로 조회
	@Query(value = "SELECT b.*, u_nickname FROM board b "
			+ "INNER JOIN users u ON b.uno = u.uno "
			+ "WHERE b_code = ?1 "
			+ "and b_title LIKE CONCAT('%', ?2, '%') "
			+ "order by b_date desc",
			countQuery = "select count(*) from board where b_code=?1 AND b_title LIKE CONCAT('%', ?2, '%')",
			nativeQuery = true)
	public Page<List<Map<String, Object>>> searchByBTitle(int b_code, String keyword, Pageable pageable);
	
	// 자유, 질문 게시판 닉네임으로 조회
		@Query(value = "SELECT b.*, u_nickname FROM board b "
				+ "INNER JOIN users u ON b.uno = u.uno "
				+ "WHERE b_code = ?1 "
				+ "and u_nickname = ?2 "
				+ "order by b_date desc",
				countQuery = "select count(*) from board b INNER JOIN users u ON b.uno = u.uno where b_code=?1 AND u_nickname = ?2",
				nativeQuery = true)
		public Page<List<Map<String, Object>>> searchByUNickname(int b_code, String keyword, Pageable pageable);
	
	// 모임 게시판 조회
	@Query(value = "SELECT b.*, u_nickname, r_name FROM board b "
			+ "INNER JOIN users u ON b.uno = u.uno INNER JOIN regioncode r ON b.rno = r.rno "
			+ "WHERE b_code = ? order by b_date desc;",
			countQuery = "select count(*) from board where b_code=?1",
			nativeQuery = true)
	public Page<List<Map<String, Object>>> findClubByBcode(int b_code, Pageable pageable);
	
//	사진 없는 게시판 메소드 끝
	
	
	//중고거래(사진형게시판) 16개씩 조회(페이징용)
    @Query(value = "SELECT * FROM board WHERE b_code = ?1 ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1",
    		nativeQuery = true)
    public Page<Board> findBoardByBCode(int b_code, Pageable pageable);
    
  //중고거래(사진형게시판) 16개씩 조회(페이징용) - 현재진행중인 만 뽑기
    @Query(value = "SELECT * FROM board WHERE b_code = ?1 AND ongoing=1 ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1 and ongoing=1",
    		nativeQuery = true)
    public Page<Board> findBoardByBCodeOngoing(int b_code, Pageable pageable);
    
    //getNextBno(게시판 구분해서)
    @Query(value ="select ifnull(max(bno),0)+1 from board where b_code = ?", nativeQuery = true)
    public int getNextBno(int b_code);
     
    //게시판 상세 -(게시판번호, 게시글번호)
    @Query(value ="SELECT b.*, u.u_nickname, u.u_fname, r.r_name FROM board b LEFT OUTER JOIN users u ON b.uno = u.uno LEFT OUTER JOIN regioncode r ON b.rno = r.rno WHERE b.bno = ?1 AND b.b_code = ?2", nativeQuery = true)
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
    
    //제목으로 검색 & 진행중
    @Query(value="SELECT * FROM board WHERE b_code = ?1 AND b_title LIKE CONCAT('%', ?2, '%') AND ongoing=1 "
    		+ "ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1 AND b_title AND ongoing=1 LIKE CONCAT('%', ?2, '%')",
    		nativeQuery = true)
    public Page<Board> searchBoardByBTitleOngoing(int b_code, String search ,Pageable pageable);
    
    //지역과 제목으로 검색
    @Query(value="SELECT * FROM board WHERE b_code = ?1 AND rno = ?2 AND b_title LIKE CONCAT('%', ?3, '%') \r\n"
    		+ "ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1 AND rno = ?2 AND b_title LIKE CONCAT('%', ?3, '%')",
    		nativeQuery = true)
    public Page<Board> searchBoardByBTitleAndRegion(int b_code ,String rno, String search, Pageable pageable);
    
    //지역과 제목으로 검색 & 진행중
    @Query(value="SELECT * FROM board WHERE b_code = ?1 AND rno = ?2 AND b_title LIKE CONCAT('%', ?3, '%') AND ongoing=1 "
    		+ "ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1 AND rno = ?2 AND b_title AND ongoing=1 LIKE CONCAT('%', ?3, '%')",
    		nativeQuery = true)
    public Page<Board> searchBoardByBTitleAndRegionOngoing(int b_code ,String rno, String search, Pageable pageable);
    
    //지역으로만 검색
    @Query(value="SELECT * FROM board WHERE b_code = ?1 AND rno = ?2 ORDER BY b_date DESC", 
    		countQuery = "select count(*) from board where b_code=?1 AND b_title LIKE CONCAT('%', ?2, '%')",
    		nativeQuery = true)
    public Page<Board> searchBoardByRegion(int b_code, String rno ,Pageable pageable);
    
    //b_code랑 bno로 삭제
    @Modifying
    @Transactional
    @Query(value="DELETE FROM board WHERE b_code = ?1 AND bno = ?2", nativeQuery = true)
    public int deleteBoard(int b_code, int bno);
    
    //게시판 코드랑 번호로 게시판 찾기
    @Query(value="SELECT * from board where b_code = ?1 and bno = ?2", nativeQuery = true)
    public Board findBoardByBnoAndBCode(int b_code, int bno);
    
    //판매완료로 변경
    @Modifying
    @Transactional
    @Query(value="UPDATE board SET ongoing=0 WHERE b_code=?1 and bno=?2", nativeQuery = true)
    public void usedgoodSold(int b_code, int bno);
    
    //고객번호로 게시글 조회 - countQuery는 Pageable를 통해서 한 페이지에 가져올 게시물의 개수를 설정하기 위함. 즉, Pageable를 사용하려면 countQuery를 해야 함.
    @Query(value = "select b.* from board b inner join users u on b.uno=u.uno where b.uno=?",countQuery = "select count(*) from board where uno=?", nativeQuery = true)
    public Page<Board> findByUno(Long uno, Pageable pageable);
    
    
    //메인페이지 전국댕댕자랑 1위~5위 정렬
    @Query(value = "select b_fname from board where b_code = 1 ORDER BY b_hit DESC LIMIT 5", nativeQuery = true)
    public List<String> findTop5ByBHit();
}

