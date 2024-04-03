package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class RegionController {

	
    @Value("${kakao.api.key}")
    private String kakaoApiKey;
    
    //병원 조회
    @GetMapping("/region/hospital")
    public void hospitalPage() {
    }
    
    //병원 상세(지도 포함)
    @GetMapping("/region/hospitalDetail")
    public void hospitalDetailPage(Model model) {
    	model.addAttribute("kakaoApiKey", kakaoApiKey);
    }
    
    
}
    
