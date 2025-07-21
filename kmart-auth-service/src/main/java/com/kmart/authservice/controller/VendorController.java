package com.kmart.authservice.controller;

import com.kmart.authservice.dto.LoginRequest;
import com.kmart.authservice.model.Vendor;
import com.kmart.authservice.security.JwtUtil;
import com.kmart.authservice.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Vendor vendor) {
        try {
            vendorService.registerVendor(vendor);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Vendor registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginVendor(@RequestBody LoginRequest loginRequest) {
        try {
            String message = vendorService.validateVendor(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok().body(Map.of("message", message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractUsername(token);
        Vendor vendor = vendorService.getVendorByEmail(email);

        return vendor != null ?
                ResponseEntity.ok(vendor) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendor not found");
    }
}
