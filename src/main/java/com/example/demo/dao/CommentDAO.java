package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Comment;

@Repository
public interface CommentDAO extends JpaRepository<Comment, Integer> {

	// 게시글 번호 추가
	@Query(value="select ifnull(max(cno),0) + 1 from comment", nativeQuery = true)
	public int getNextCno();
	
	// 해당 게시물을 댓글 리스트
	@Query(value = "select c.*, u_nickname, u_fname from comment c "
			+ "inner join users u on c.uno = u.uno "
			+ "where b_code = ?1 and bno = ?2 order by c_ref, c_step"  ,
			countQuery = "select count(*) from comment where b_code = ?1 and bno = ?2",
			nativeQuery = true)
	public List<Map<String, Object>> listComment(int b_code, int bno);
	
	//동일 c_ref인 것들 중 c_step의 최대값 가져오기 
	@Query(value="select ifnull(max(c_step),0) from comment where b_code = ?1 and bno = ?2 and c_ref=?3", nativeQuery = true)
	public int getMaxCStep(int b_code, int bno, int c_ref);
	
	
}
