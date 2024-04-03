package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class RegionController {

	//병원
    @Value("${kakao.api.key}")
    private String kakaoApiKey;
    
    
    @GetMapping("/region/hospital")
    public void hospitalPage() {
    }
    @GetMapping("/region/hospitalDetail")
    public void hospitalDetailPage(Model model) {
    	model.addAttribute("kakaoApiKey", kakaoApiKey);
    }
    //-병원
    
    
}
    
