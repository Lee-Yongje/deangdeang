package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="regioncode")
public class RegionCode {
	
	public RegionCode() {
	}
	
	public RegionCode(String rno) {
		super();
		this.rno = rno;
	}
	
	@Id
	private String rno;
	
	private String r_name;
}
