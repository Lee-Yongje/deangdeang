package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.example.demo.dto.LoginFormDTO;
import com.example.demo.entity.KakaoProfile;
import com.example.demo.entity.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {
	
    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

//    @GetMapping("/login")
//    public void showLoginForm(){}
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
    	System.out.println("로그인컨트롤러동ㅎ작함");
        model.addAttribute("loginForm", new LoginFormDTO());
        model.addAttribute("clientId", clientId);
        model.addAttribute("redirectUri", redirectUri);
        return "login";
    }

//    @PostMapping("/login")
//    public String processLogin(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
//        try {
//            Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//            );
//            SecurityContextHolder.getContext().setAuthentication(auth);
//            return "redirect:/index";
//        } catch (AuthenticationException e) {
//            model.addAttribute("loginError", "Invalid username or password.");
//            return "login";
//        }
//    }
    
    
    @GetMapping("/login-error")
    public String loginError(@RequestParam Optional<Boolean> error, Model model) {
        if (error.isPresent() && error.get()) {
            model.addAttribute("loginError", "Invalid username or password.");
        }
        return "login";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess(Model model) {
        model.addAttribute("logoutMessage", "You have been successfully logged out.");
        return "login";
    }

}

//    @PostMapping
//    public ModelAndView login(@RequestParam String username, @RequestParam String password) {
//        try {
//            Authentication request = new UsernamePasswordAuthenticationToken(username, password);
//            Authentication result = authenticationManager.authenticate(request);
//            SecurityContextHolder.getContext().setAuthentication(result);
//            return new ModelAndView("redirect:/index");
//        } catch (AuthenticationException e) {
//            ModelAndView modelAndView = new ModelAndView("login");
//            modelAndView.addObject("error", "Invalid username or password.");
//            return modelAndView;
//        }
//    }
