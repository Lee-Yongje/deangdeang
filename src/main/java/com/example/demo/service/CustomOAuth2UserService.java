package com.example.demo.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserRepository;
import com.example.demo.entity.AuthType;
import com.example.demo.entity.User;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuthUser(oAuth2User);
    }

    private OAuth2User processOAuthUser(OAuth2User oAuth2User) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = profile != null ? (String) profile.get("nickname") : "Default Nickname";
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : "No Email Provided";

        // Use these details to update your user management system or database
        System.out.println("Nickname: " + nickname);
        System.out.println("Email: " + email);

        return oAuth2User;
    }
//    private OAuth2User processOAuthUser(OAuth2User oAuth2User) {
//        // Extract details from OAuth2User and convert them to your User entity
//        // Save or update user in your database
//        String email = oAuth2User.getAttribute("email");
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            user = new User();
//            user.setEmail(email);
//            user.setAuthType(AuthType.KAKAO);
//            userRepository.save(user);
//        }
//        return oAuth2User;
//    }
}