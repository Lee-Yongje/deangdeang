package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.entity.User;
import com.example.demo.entity.AuthType;
import com.example.demo.entity.KakaoProfile;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with STANDARD authentication type.
     * @param userDto the data transfer object containing user details
     * @return the saved user entity
     */
    public User registerNewStandardUser(User userDto) {
        // Check if user already exists
        if (emailExist(userDto.getEmail())) {
            throw new IllegalStateException("Email already exists!");
        }

        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(userDto.getPasswordHash()));
        newUser.setPhone(userDto.getPhone());
        newUser.setNickname(userDto.getNickname());
        newUser.setFilename(userDto.getFilename());
        newUser.setRegionCode(userDto.getRegionCode());
        newUser.setAuthType(AuthType.STANDARD);

        return userRepository.save(newUser);
    }

    public User registerOrUpdateKakaoUser(KakaoProfile profile) {
        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        if (user == null) {
            user = new User();
            user.setEmail(profile.getKakao_account().getEmail());
            user.setNickname(profile.getProperties().getNickname());
            user.setAuthType(AuthType.KAKAO);
            userRepository.save(user);
        } else {
            // Update existing user details if needed
            user.setNickname(profile.getProperties().getNickname());
            userRepository.save(user);
        }
        return user;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}


