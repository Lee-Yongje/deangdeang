package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommunityController {
	//----------게시판형(자유,질문,모임)----------
	@GetMapping("/community/board")
	public void boardPage(String b_name, HttpSession session, Model model) { 
		model.addAttribute("b_name", b_name);
	}
	
	
	
	
	//-----------사진형--------------
	//전국댕댕자랑 조회
	@GetMapping("/community/boast")
    public void boastPage() {
    }
	
	//신고제보 조회
    @GetMapping("/community/report")
    public void reportPage() {
    }
   
    //전국댕댕자랑 상세
    @GetMapping("/member/community/boastDetail")
    public void boastDetailPage() {
    }
    
    //신고제보 상세
    @GetMapping("/member/community/reportDetail")
    public void reportDetail() {
    }
    
    
    
    //---------게시판형, 사진형 공통---------------
    //글 작성
    @GetMapping("/member/community/boardWrite")
    public void boardWritePage(String b_name, HttpSession session, Model model) {
    	model.addAttribute("b_name", b_name);
    }
    
}
    
