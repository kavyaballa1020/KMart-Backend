package com.kmart.authservice.service;

import com.kmart.authservice.model.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import com.kmart.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    public void registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // ✅ Send confirmation email
        emailService.sendRegistrationEmail(user.getEmail(), user.getFirstName());
    }

    
    public boolean validateUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            System.out.println("❌ User not found: " + email);
            return false;
        }

        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());

        if (matches) {
            System.out.println("✅ User authenticated: " + email);
        } else {
            System.out.println("❌ Invalid user password for: " + email);
        }

        return matches;
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }
    public void updateProfilePicture(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfilePicture(file.getBytes());
        userRepository.save(user);
    }



}
