package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class UsedgoodController {
	
	 //중고거래 조회
	 @GetMapping("/usedgood/usedgood")
	    public void usedgoodPage() {
	    }
    
	 //중고거래 상세
	 @GetMapping("/member/usedgood/detail")
	    public void usedgoodDetailPage() {
	    }
	 
	 //중고거래 글 등록
	 @GetMapping("/member/usedgood/write")
	    public void usedgoodWritePage() {
	    }
}
    
