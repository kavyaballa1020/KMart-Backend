package com.kmart.authservice.service;

import com.kmart.authservice.model.Vendor;
import com.kmart.authservice.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;  // ✅ Inject EmailService

    public void registerVendor(Vendor vendor) {
        if (vendorRepository.findByEmail(vendor.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        vendor.setPassword(passwordEncoder.encode(vendor.getPassword()));
        vendorRepository.save(vendor);

        // ✅ Send welcome email
        emailService.sendRegistrationEmail(vendor.getEmail(), vendor.getUsername());
    }

    public boolean validateVendor(String email, String password) {
        Vendor vendor = vendorRepository.findByEmail(email).orElse(null);
        return vendor != null && passwordEncoder.matches(password, vendor.getPassword());
    }

    public Vendor getVendorByEmail(String email) {
        return vendorRepository.findByEmail(email).orElse(null);
    }
}
