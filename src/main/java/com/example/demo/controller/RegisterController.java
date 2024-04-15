package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.RegionCodeDAO;
import com.example.demo.entity.RegionCode;
import com.example.demo.service.RegionCodeService;

@Controller
public class RegisterController {
	@Autowired
	private RegionCodeService regionCodeService;
	
	@Autowired
	private RegionCodeDAO regionCodeDAO;
	
	
    @GetMapping("/register_success")
    public String registerSuccessPage() {
        return "register_success";
    }

    @GetMapping("/register_kakao")
    public String registerFormKakao(@RequestParam("username") String email, Model model) {
        model.addAttribute("email", email);
        List<RegionCode> regionCodes = regionCodeService.findAll();
        model.addAttribute("regionCodes", regionCodes);
        return "register_kakao"; // Return the name of your Thymeleaf template
    }
    
    @GetMapping("/register")
    public String registerFormStandard() {
        return "register";
    }
    
}
