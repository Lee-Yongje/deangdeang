package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.RegionCodeDAO;
import com.example.demo.dto.LoginFormDTO;
import com.example.demo.entity.RegionCode;
import com.example.demo.service.BoardService;

@RestController
public class RegionCodeController {
	
	@Autowired
	private RegionCodeDAO dao;
	
    @GetMapping("/regionCode")
    public List<RegionCode> getAllRegionCodes() {
        return dao.findAll();
    }
}
    
