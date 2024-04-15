package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="boardcode")
public class BoardCode {
	
	@Id
	private int b_code;
	
	private String b_name;
}
