package com.kmart.authservice.service;

import com.kmart.authservice.model.Admin;
import com.kmart.authservice.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;  // ✅ Inject EmailService

    public void registerAdmin(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);

        // ✅ Send welcome email
        emailService.sendRegistrationEmail(admin.getEmail(), admin.getUsername());
    }

    public boolean validateAdmin(String email, String rawPassword) {
        Admin admin = adminRepository.findByEmail(email).orElse(null);

        if (admin == null) {
            System.out.println("❌ Admin not found: " + email);
            return false;
        }

        boolean matches = passwordEncoder.matches(rawPassword, admin.getPassword());

        if (matches) {
            System.out.println("✅ Admin authenticated: " + email);
        } else {
            System.out.println("❌ Invalid admin password for: " + email);
        }

        return matches;
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }
}
