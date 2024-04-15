package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.RegionCode;
import com.example.demo.dao.RegionCodeDAO;

@Service
public class RegionCodeService {

	@Autowired
    private RegionCodeDAO regionCodeDAO;
    
    
    public List<RegionCode> findAll() {
        // Call the repository method to fetch all region codes
        return regionCodeDAO.findAll();
    }
}