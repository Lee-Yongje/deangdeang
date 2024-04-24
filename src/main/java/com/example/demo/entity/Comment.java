package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="comment")
public class Comment {
	
	@Id
	private int cno;
	// 내용
	private String c_content;
	// 레벨
	private int c_level;
	// 원 댓글
	private int c_ref;
	// 부모댓글번호
	private int c_pcno;
	// 댓글 순서
	private int c_step;
	// 날짜
	private LocalDateTime c_date;
	
	// 작성한 댓글은 게시판 위치
	@Embedded
	private BoardId id;
	
	// 댓글 작성자 번호
	@ManyToOne
	@JoinColumn(name="uno")
	private Users user; 
}
