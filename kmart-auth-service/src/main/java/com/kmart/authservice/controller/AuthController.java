package com.kmart.authservice.controller;

import com.kmart.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/users/role")
    public ResponseEntity<String> getUserRole(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String role = jwtUtil.extractRole(jwt);
        return ResponseEntity.ok(role);
    }
}
