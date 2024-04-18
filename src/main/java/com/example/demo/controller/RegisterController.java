package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.RegionCodeDAO;
import com.example.demo.entity.RegionCode;
import com.example.demo.entity.Users;
import com.example.demo.service.RegionCodeService;
import com.example.demo.service.RegisterService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
    public String registerFormStandard(Model model) {
    	List<RegionCode> regionCodes = regionCodeService.findAll(); 
        model.addAttribute("regionCodes", regionCodes);
        return "register";
    }
    

    @PostMapping("/register")
    public String registerUser(Users user, @RequestParam("regionCode") String regionCode, 
                               @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        // RegionCode 객체 생성 및 설정
        RegionCode rc = new RegionCode();
        rc.setRno(regionCode);
        user.setRegionCode(rc);

        // 파일 처리
        if (!file.isEmpty()) {
            try {
                String filename = registerService.storeFile(file); // Separated file storage logic
                user.setFilename(filename); // Set the filename to the user entity
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("message", "파일 업로드에 실패했습니다: " + e.getMessage());
                return "redirect:/register";
            }
        }

        // 유저 정보 저장
        try {
            registerService.registerUser(user); // Only handles user saving now
            redirectAttributes.addFlashAttribute("message", "성공적으로 등록되었습니다!");
            return "redirect:/register_success"; // 성공 시 리디렉션될 페이지
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "등록 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/register";
        }
    }

    
}
