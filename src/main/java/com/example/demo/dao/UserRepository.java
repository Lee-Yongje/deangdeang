package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Users;


import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    
    @Modifying
	@Query(value = "update users set u_name=?, u_email=?, u_phone=?, u_nickname=?, u_fname=?, rno=? where uno=?", nativeQuery = true)
	@Transactional
	public int updateInfo(String u_name, String u_email, String u_phone, String u_nickname, String u_fname, String rno, Long uno);

	@Modifying
	@Query(value = "update users set u_pwd=? where uno=?", nativeQuery = true)
	@Transactional
	public void updatePwd(String newPwd, Long uno);
}