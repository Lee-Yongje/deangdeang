package com.example.demo.entity;


import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;


@Data
@Entity
@Table(name="diary")
public class Diary {
	
	@Id
	private int dno;
	
	private String d_title;
	private String d_content;
	private LocalDateTime d_date;
	private String d_fname;
	
	@ManyToOne
	@JoinColumn(name="uno")
	private Users users;
	
	 
	@Transient	
	private MultipartFile uploadFile;
}
