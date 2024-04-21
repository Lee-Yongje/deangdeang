package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name="board")
public class Board {

	@EmbeddedId
	private BoardId id;
	
	@MapsId("b_code")
	@ManyToOne
	@JoinColumn(name="b_code")
	private BoardCode boardcode;
	
	
	private String b_title;
	private String b_content;
	private String b_fname;
	private LocalDateTime b_date;
	
	@Column(nullable = true) //중고장터 외에는 b_price는 null이 가능해서 Null이 가능하게 하기 위함
	private Integer b_price; 
	
	@Column(nullable = true) 
	private Integer ongoing; 
	
	private int b_hit;
	
	@ManyToOne
	@JoinColumn(name="uno")
	private Users user; 
	
	@ManyToOne
	@JoinColumn(name="rno")
	private RegionCode regionCode;
	
	
	
    @Transient //테이블에서 제외
	private MultipartFile uploadFile; 
}
