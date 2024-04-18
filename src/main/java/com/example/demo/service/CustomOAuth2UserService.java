package com.example.demo.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UsersDAO;
import com.example.demo.entity.AuthType;
import com.example.demo.entity.Users;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UsersDAO userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuthUser(oAuth2User);
    }

    private OAuth2User processOAuthUser(OAuth2User oAuth2User) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
        if (kakaoAccount == null) {
            throw new OAuth2AuthenticationException("No kakao account data available");
        }

        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not provided by Kakao");
        }
        
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            // User not found, create a new user
            user = new Users();
            user.setEmail(email);
            user.setAuthType(AuthType.KAKAO);
            // Set other necessary default values or extract from kakaoAccount if available
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                user.setNickname((String) profile.get("nickname"));
            } else {
                user.setNickname("Default Nickname");
            }
        } else {
            // Update existing user's details
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                user.setNickname((String) profile.get("nickname"));
            }
        }
        
        userRepository.save(user);
        return oAuth2User; // You might need to convert this to your own UserDetails implementation if needed
    }

}

//private OAuth2User processOAuthUser(OAuth2User oAuth2User) {
//// Extract details from OAuth2User and convert them to your User entity
//// Save or update user in your database
//String email = oAuth2User.getAttribute("email");
//User user = userRepository.findByEmail(email);
//if (user == null) {
//  user = new User();
//  user.setEmail(email);
//  user.setAuthType(AuthType.KAKAO);
//  userRepository.save(user);
//}
//return oAuth2User;
//}