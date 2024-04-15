package com.example.demo.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import org.apache.catalina.User;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name="schedule")
public class Schedule {
	
	@Id
	private int sno;
	
	private Date s_date;
	private String s_content;
	private String s_complete;
	
	@ManyToOne
	@JoinColumn(name="uno")
	private Users users;
	
	@ManyToOne
	@JoinColumn(name="pno")
	private Puppy puppy;
	

}
