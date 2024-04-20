package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.PuppyDAO;
import com.example.demo.entity.Puppy;

@Service
public class PuppyService {
	
	@Autowired
	private PuppyDAO dao;
	
	
	public List<Puppy> findByUno(Long uno) {
		return dao.findByUno(uno);
	}
	
	public int getNextPno() {
		return dao.getNextPno();
	}
	
	public Puppy save(Puppy p) {
		return dao.save(p);
	}
	
	public Puppy findByPno(int pno) {
		return dao.findById(pno).orElse(null);
	}
	
	public int deletePuppy(int pno) {
		return dao.deletePuppy(pno);
	}

}