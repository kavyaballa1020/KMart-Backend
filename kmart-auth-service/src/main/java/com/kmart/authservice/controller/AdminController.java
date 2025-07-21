package com.kmart.authservice.controller;

import com.kmart.authservice.dto.LoginRequest;
import com.kmart.authservice.model.Admin;
import com.kmart.authservice.model.Vendor;
import com.kmart.authservice.model.VendorStatus;
import com.kmart.authservice.security.JwtUtil;
import com.kmart.authservice.service.AdminService;
import com.kmart.authservice.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private JwtUtil jwtUtil;

    // Admin Registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Admin admin) {
        try {
            adminService.registerAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Admin registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Admin Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isValid = adminService.validateAdmin(loginRequest.getEmail(), loginRequest.getPassword());

        if (isValid) {
            String token = jwtUtil.generateToken(loginRequest.getEmail(), "ROLE_ADMIN");
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "type", "Bearer",
                    "email", loginRequest.getEmail()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }

    // Admin Profile
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);
        Admin admin = adminService.getAdminByEmail(email);

        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }
    }

    // Extract role from JWT
    @GetMapping("/api/users/role")
    public ResponseEntity<String> getUserRole(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String role = jwtUtil.extractRole(jwt);
        return ResponseEntity.ok(role);
    }

    // ✅ Get All Pending Vendors
    @GetMapping("/vendors/pending")
    public ResponseEntity<List<Vendor>> getPendingVendors() {
        List<Vendor> pending = vendorService.getVendorsByStatus(VendorStatus.PENDING);
        return ResponseEntity.ok(pending);
    }

    // ✅ Approve Vendor
    @PostMapping("/vendors/{id}/approve")
    public ResponseEntity<?> approveVendor(@PathVariable Long id) {
        try {
            vendorService.approveVendor(id);
            return ResponseEntity.ok(Map.of("message", "Vendor approved successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Reject Vendor
    @PostMapping("/vendors/{id}/reject")
    public ResponseEntity<?> rejectVendor(@PathVariable Long id) {
        try {
            vendorService.rejectVendor(id);
            return ResponseEntity.ok(Map.of("message", "Vendor rejected successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
