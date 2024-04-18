package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UsersDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.entity.Users;
import com.example.demo.entity.AuthType;
import com.example.demo.entity.KakaoProfile;

@Service
public class UsersService {

    @Autowired
    private UsersDAO us;

    @Autowired
	private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with STANDARD authentication type.
     * @param userDto the data transfer object containing user details
     * @return the saved user entity
     */
    public Users registerNewStandardUser(Users userDto) {
        // Check if user already exists
        if (emailExist(userDto.getEmail())) {
            throw new IllegalStateException("Email already exists!");
        }

        Users newUser = new Users();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(userDto.getPasswordHash()));
        newUser.setPhone(userDto.getPhone());
        newUser.setNickname(userDto.getNickname());
        newUser.setFilename(userDto.getFilename());
        newUser.setRegionCode(userDto.getRegionCode());
        newUser.setAuthType(AuthType.STANDARD);

        return us.save(newUser);
    }

    public Users registerOrUpdateKakaoUser(KakaoProfile profile) {
        Users user = us.findByEmail(profile.getKakao_account().getEmail());
        if (user == null) {
            user = new Users();
            user.setEmail(profile.getKakao_account().getEmail());
            user.setNickname(profile.getProperties().getNickname());
            user.setAuthType(AuthType.KAKAO);
            us.save(user);
        } else {
            // Update existing user details if needed
            user.setNickname(profile.getProperties().getNickname());
            us.save(user);
        }
        return user;
    }

    private boolean emailExist(String email) {
        return us.findByEmail(email) != null;
    }
    
    public Users findByEmail(String email) {
        return us.findByEmail(email);
    }
    
   
	public Users findById(long uno) {
		return us.findById(uno).orElse(null); // .orElse(null)을 사용하는 이유 : finById(1)를 조회했을 때 값이 없으면 null로 처리하기 위함.
	}
	
	public int updateInfo(String u_name, String u_email, String u_phone, String u_nickname, String u_fname, String rno, Long uno) {
		return us.updateInfo(u_name, u_email, u_phone, u_nickname, u_fname, rno, uno);
	}
	
	public void updatePwd(String newPwd,Long uno) {
		us.updatePwd(newPwd, uno);
	}
}


