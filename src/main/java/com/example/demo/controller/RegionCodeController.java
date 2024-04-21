package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.RegionCodeDAO;
import com.example.demo.entity.RegionCode;

@RestController
public class RegionCodeController {

	@Autowired
	private RegionCodeDAO dao;

	@GetMapping("/region/regionCode")
	public List<RegionCode> getAllRegionCodes() {
		return dao.findAll();
	}
}
