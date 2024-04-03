package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class UsedgoodController {

	 @GetMapping("/usedgood/usedgood")
	    public void usedgoodPage() {
	    }
    
	 @GetMapping("/member/usedgood/usedgoodDetail")
	    public void usedgoodDetailPage() {
	    }
	 
	 @GetMapping("/member/usedgood/usedgoodWrite")
	    public void usedgoodWritePage() {
	    }
}
    
