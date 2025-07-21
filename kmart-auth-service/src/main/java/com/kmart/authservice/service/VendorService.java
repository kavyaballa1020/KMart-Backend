package com.kmart.authservice.service;

import com.kmart.authservice.model.Vendor;
import com.kmart.authservice.model.VendorStatus;
import com.kmart.authservice.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // ✅ Register vendor with PENDING status
    public void registerVendor(Vendor vendor) {
        if (vendorRepository.findByEmail(vendor.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        vendor.setPassword(passwordEncoder.encode(vendor.getPassword()));
        vendor.setStatus(VendorStatus.PENDING);
        vendor.setApproved(false); // ✅ Important
        vendorRepository.save(vendor);

        emailService.sendRegistrationEmail(vendor.getEmail(), vendor.getUsername());
    }


    // ✅ Only allow login if status is APPROVED
    public String validateVendor(String email, String password) {
        Optional<Vendor> optionalVendor = vendorRepository.findByEmail(email);

        if (optionalVendor.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        Vendor vendor = optionalVendor.get();

        if (!vendor.isApproved()) {
            throw new RuntimeException("Vendor not approved by admin");
        }

        if (!passwordEncoder.matches(password, vendor.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return "Login successful";
    }


    public Vendor getVendorByEmail(String email) {
        return vendorRepository.findByEmail(email).orElse(null);
    }

    // ✅ Get vendors by status (e.g. pending)
    public List<Vendor> getVendorsByStatus(VendorStatus status) {
        return vendorRepository.findByStatus(status);
    }

    // ✅ Approve vendor
    public void approveVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new RuntimeException("Vendor not found"));
        vendor.setStatus(VendorStatus.APPROVED);
        vendorRepository.save(vendor);

        emailService.sendApprovalEmail(vendor.getEmail(), vendor.getUsername());
    }

    // ✅ Reject vendor
    public void rejectVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new RuntimeException("Vendor not found"));
        vendor.setStatus(VendorStatus.REJECTED);
        vendorRepository.save(vendor);

        emailService.sendRejectionEmail(vendor.getEmail(), vendor.getUsername());
    }
}
