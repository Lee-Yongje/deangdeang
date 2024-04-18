package com.example.demo.service;

import com.example.demo.dao.RegisterDAO;
import com.example.demo.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class RegisterService {

    private final Path rootLocation = Paths.get("src/main/resources/static/images/photo");
    
    @Autowired
    private RegisterDAO registerDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(Users user) {
        // Encode and set the password hash
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        // Save the user to the database
        registerDAO.save(user);
    }

    @Transactional
    public String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            // This is a security check
            throw new IOException("Cannot store file with relative path outside current directory " + filename);
        }
        // Copy the file to the target location (Replacing existing file with the same name)
        Path resolvePath = this.rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), resolvePath, StandardCopyOption.REPLACE_EXISTING);
        
        return filename; // Return the filename after storing it
    }
}

//@Service
//public class RegisterService {
//
//    private final Path rootLocation = Paths.get("src/main/resources/static/images/photo");
//    
//    @Autowired
//    private RegisterDAO registerDAO;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public void registerUser(Users user, MultipartFile file) throws IOException {
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        try {
//            if (file.isEmpty()) {
//                throw new IOException("Failed to store empty file " + filename);
//            }
//            if (filename.contains("..")) {
//                // This is a security check
//                throw new IOException("Cannot store file with relative path outside current directory " + filename);
//            }
//            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
//            user.setFilename(filename);
//            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash())); // Encoding the password
//            registerDAO.save(user);
//        } catch (IOException e) {
//            throw new IOException("Failed to store file " + filename, e);
//        }
//    }
//}
