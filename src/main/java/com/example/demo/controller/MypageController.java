package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class MypageController {

	@GetMapping("/member/mypage/changeInfo")
    public void changeInfoPage() {
    }
    @GetMapping("/member/mypage/changePwd")
    public void changePwdPage() {
    }
    @GetMapping("/member/mypage/insertPuppy")
    public void insertPuppyPage() {
    }
    @GetMapping("/member/mypage/listPuppy")
    public void listPuppyForm(Model model) {
    }
    @GetMapping("/member/mypage/myPosts")
    public void myPostsPage() {
    }
    
}
    
