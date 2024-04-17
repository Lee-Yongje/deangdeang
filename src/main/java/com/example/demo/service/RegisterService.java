package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.dao.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class RegisterService {

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Setting the root location to /image/photo/
    private final Path rootLocation = Paths.get("src/main/resources/static/images/photo");

    public Users registerUser(Users user, MultipartFile file) throws IOException {
        String filename = storeFile(file);
        user.setFilename(filename);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash())); // Encoding password
        return registerRepository.save(user);
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        
        // Generate a unique filename with UUID to prevent duplication
        String originalFilename = file.getOriginalFilename();
        String filename = "photo_" + originalFilename;
        Path destinationFile = this.rootLocation.resolve(Paths.get(filename))
                .normalize().toAbsolutePath();
        
        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new RuntimeException("Cannot store file outside current directory.");
        }
        
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile);
        }
        return filename;
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }
}