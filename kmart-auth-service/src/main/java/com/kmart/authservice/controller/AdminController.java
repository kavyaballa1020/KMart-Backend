package com.kmart.authservice.controller;

import com.kmart.authservice.dto.LoginRequest;
import com.kmart.authservice.model.Admin;
import com.kmart.authservice.security.JwtUtil;
import com.kmart.authservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Admin admin) {
        try {
            adminService.registerAdmin(admin);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

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
    @GetMapping("/api/users/role")
    public ResponseEntity<String> getUserRole(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String role = jwtUtil.extractRole(jwt);
        return ResponseEntity.ok(role);
    }


}
