package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.example.demo.service.UsersService;
import com.example.demo.entity.Users;

import java.util.Collections;

@Component
public class KakaoAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsersService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String kakaoUser = authentication.getName();
        String kakaoPass = authentication.getCredentials().toString();

        if (isValidKakaoUser(kakaoUser, kakaoPass)) {
            Users user = userService.findByEmail(kakaoUser);
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            } else {
                throw new UsernameNotFoundException("No user registered with this Kakao ID");
            }
        } else {
            throw new UsernameNotFoundException("Invalid Kakao credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean isValidKakaoUser(String kakaoId, String kakaoPass) {
        // Implement the actual validation logic here
        return true; // Assuming always true for demonstration purposes
    }
}