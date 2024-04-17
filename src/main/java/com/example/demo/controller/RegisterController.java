package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.RegionCodeDAO;
import com.example.demo.entity.RegionCode;
import com.example.demo.entity.Users;
import com.example.demo.service.RegionCodeService;
import com.example.demo.service.RegisterService;

@Controller
public class RegisterController {
	@Autowired
    private RegisterService registerService;

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
    
    @PostMapping("/register_kakao")
    public ModelAndView registerUserKakao(@RequestParam("email") String email, // Add other necessary parameters
                                          @RequestParam("regionCode") Long regionCodeId) {
        // Kakao registration logic should be implemented here
        return new ModelAndView("redirect:/register_success");
    }
    
    @GetMapping("/register")
    public String registerFormStandard() {
        return "register";
    }
    
    @PostMapping("/register")
    public ModelAndView registerUserStandard(@RequestParam("id") String id,
                                             @RequestParam("password") String password,
                                             @RequestParam("email") String email,
                                             @RequestParam("phone") String phone,
                                             @RequestParam("nickname") String nickname,
                                             @RequestParam("file") MultipartFile file) {
        Users user = Users.builder()
                .name(id)
                .passwordHash(password)
                .email(email)
                .phone(phone)
                .nickname(nickname)
                .build();

        try {
            Users registeredUser = registerService.registerUser(user, file);
            return new ModelAndView("redirect:/register_success");
        } catch (IOException e) {
            return new ModelAndView("error", "message", "File upload failed: " + e.getMessage());
        }
    }
    
}
