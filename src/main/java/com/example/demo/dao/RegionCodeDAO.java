package com.example.demo.dao;

import com.example.demo.entity.RegionCode;
import com.example.demo.entity.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionCodeDAO extends JpaRepository<RegionCode, String> {


	@Query(value="select * from regioncode where rno=?", nativeQuery = true)
	public RegionCode findByRno(String rno);
}

