package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class LoginController {
	@GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginFormDTO()); // LoginForm은 사용자 입력을 담을 클래스
        return "login";
    }

    @GetMapping("/member/login")
    public String userLogin() {
        return "/member/index";
    }
}
