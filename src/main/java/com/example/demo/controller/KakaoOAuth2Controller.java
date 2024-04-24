package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.KakaoProfile;
import com.example.demo.entity.OAuthToken;
import com.example.demo.entity.Users;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.KakaoOAuth2Service;
import com.example.demo.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SessionAttributes("userSession")
@Controller
public class KakaoOAuth2Controller {
	
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    
    @Value("${kakao.login.pass}")
    private String kakaoGeneralPassword;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private CustomUserDetailsService cud;
    
    @Autowired
    private KakaoOAuth2Service kakaoOAuth2Service;

    @Autowired
    private UsersService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code, HttpSession session, RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) throws JsonProcessingException {
        // OAuth 콜백 호출 시 세션 ID를 출력
        System.out.println("Session ID post-authentication: " + session.getId());
       
        
        // REST 통신을 위한 RestTemplate 객체 생성
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 카카오 토큰 요청을 위한 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // 토큰 요청
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        
        
        // JSON 응답을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);

        // 사용자 프로필 요청을 위한 새로운 RestTemplate
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 사용자 프로필 요청
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);
        ResponseEntity<String> response2 = rt2.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoProfileRequest,
            String.class
        );

        // 사용자 프로필 정보 파싱
        KakaoProfile kakaoProfile = objectMapper.readValue(response2.getBody(), KakaoProfile.class);
        String email = kakaoProfile.getKakao_account().getEmail(); // 사용자 이메일 으로 사용

        Users existingUser = userService.findByEmail(email);//사용자 객체
        Users kakaoAuthUser = userService.findById(100); //카카오 DB인증 계정 객체
        String kakaoUser = kakaoAuthUser.getEmail(); //카카오 DB인증 계정 이메일
        String kakaoPass = kakaoAuthUser.getPasswordHash(); //카카오 DB인증 계정 패스워드

        System.out.println("이메일체크"+ email);

        // 닉네임 URL 인코딩
        String nicknameRaw = kakaoProfile.getProperties().getNickname();
        String nickname = "";
        try {
            nickname = URLEncoder.encode(nicknameRaw, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 신규 사용자 등록 또는 기존 사용자 확인
        System.out.println("존재하는 사용자:" + existingUser);        
        if (existingUser == null) {
            redirectAttributes.addAttribute("email", email);
            redirectAttributes.addAttribute("nickname", nickname);
    		return "redirect:/register_kakao";
        }
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);        
		System.out.println("인증완료");
		
		session.setAttribute("userSession", existingUser);
		System.out.println("세션적용됨");
		
		session.setAttribute("email", email);
		session.setAttribute("password", kakaoGeneralPassword);
		
		return "redirect:/Login";
    }

}