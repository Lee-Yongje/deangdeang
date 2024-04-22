package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.KakaoProfile;
import com.example.demo.entity.OAuthToken;
import com.example.demo.entity.Users;
import com.example.demo.service.KakaoOAuth2Service;
import com.example.demo.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class KakaoOAuth2Controller {
	
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private KakaoOAuth2Service kakaoOAuth2Service;

    @Autowired
    private UsersService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code, HttpSession session, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);

        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);
        ResponseEntity<String> response2 = rt2.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoProfileRequest,
            String.class
        );

        KakaoProfile kakaoProfile = objectMapper.readValue(response2.getBody(), KakaoProfile.class);
        String email = kakaoProfile.getKakao_account().getEmail(); // 이메일을 사용자 이름으로 사용
        Users existingUser = userService.findByEmail(email);


        String nicknameRaw = kakaoProfile.getProperties().getNickname();
        String nickname = "";
		try {
			nickname = URLEncoder.encode(nicknameRaw, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

//		UUID garbagePassword = UUID.randomUUID();
//        String password = garbagePassword.toString(); // 랜덤 비밀번호를 생성

        // 사용자가 이미 등록되어 있는지 확인
        System.out.println("존재하는 사용자:" + existingUser);        
        if (existingUser == null) {
        	 redirectAttributes.addAttribute("email", email);
             redirectAttributes.addAttribute("nickname", nickname);
        	return "redirect:/register_kakao";
        }
        
       
        
        // 등록된 사용자인 경우 로그인 처리
        authenticateUser(email);
        String passwordHash = existingUser.getPasswordHash();
        

        // 등록된 사용자인 경우 로그인 처리
        session.setAttribute("userSession", existingUser);
        
        // Set authentication in security context
        Authentication authToken = new UsernamePasswordAuthenticationToken(email, null, null);
        authToken.setAuthenticated(true);  // Since the user is authenticated by Kakao
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return "redirect:/index"; // Proceed to 
        
    }
    
    private void authenticateUser(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}