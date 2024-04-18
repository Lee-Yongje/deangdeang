package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Users;

@Repository
public interface UsersDAO extends JpaRepository<Users, Long> {
	
	// 세션에 유지한 Name 으로 Uno 가져오기
	@Query(value="select uno from users where u_name=?1", nativeQuery = true)
	public Long findByUName(String u_name);
}
