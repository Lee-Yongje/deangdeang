package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class CommunityController {
	//게시판형
	
	
	
	
	
	//사진형
	@GetMapping("/community/boast")
    public void boastPage() {
    }
	
    @GetMapping("/community/report")
    public void reportPage() {
    }
   

    @GetMapping("/member/community/boastDetail")
    public void boastDetailPage() {
    }
    
    @GetMapping("/member/community/reportDetail")
    public void reportDetail() {
    }
    
    
    
    //공통:글 작성
    @GetMapping("/member/community/boardWrite")
    public void boardWritePage() {
    }
    
}
    
