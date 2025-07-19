package com.kmart.authservice.controller;

import com.kmart.authservice.dto.LoginRequest;
import com.kmart.authservice.model.User;
import com.kmart.authservice.security.JwtUtil;
import com.kmart.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import java.util.Base64;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Registration failed due to server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isValid = userService.validateUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (isValid) {
            String token = jwtUtil.generateToken(loginRequest.getEmail(), "ROLE_USER");
            return ResponseEntity.ok(Map.of(
                "token", token,
                "type", "Bearer",
                "email", loginRequest.getEmail()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("phoneNumber", user.getPhoneNumber());

        if (user.getProfilePicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(user.getProfilePicture());
            response.put("profilePicture", base64Image);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody User updatedUser) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(token);

            User existingUser = userService.getUserByEmail(email);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

            userService.saveUser(existingUser);

            return ResponseEntity.ok("User profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating profile: " + e.getMessage());
        }
    }

    @PutMapping("/upload-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestPart("file") MultipartFile file,
            Principal principal) {
        try {
            userService.updateProfilePicture(principal.getName(), file);
            return ResponseEntity.ok("Profile picture uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image.");
        }
    }
    @GetMapping("/api/users/role")
    public ResponseEntity<String> getUserRole(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String role = jwtUtil.extractRole(token);
        return ResponseEntity.ok(role);
    }

}
