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
	@Query(value = "select c.*, u.u_name, u.u_fname from comment c "
			+ "inner join users u on c.uno = u.uno "
			+ "where b_code = ?1 and bno = ?2"  ,
			countQuery = "select count(*) from comment where b_code = ?1 and bno = ?2",
			nativeQuery = true)
	public List<Map<String, Object>> listComment(int b_code, int bno);
}
