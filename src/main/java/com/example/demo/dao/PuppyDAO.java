package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Puppy;

import jakarta.transaction.Transactional;

@Repository
public interface PuppyDAO extends JpaRepository<Puppy, Integer> {

	@Query(value = "select p.* from puppy p inner join users u on p.uno=u.uno where p.uno=?", nativeQuery = true)
	public List<Puppy> findByUno(int uno);
	
	@Query(value = "select ifnull(max(pno),0)+1 from puppy", nativeQuery = true)
	public int getNextPno();
	
}
