//authController.java
package com.example.authdemo.controller;

import com.example.authdemo.dto.LoginRequest;
import com.example.authdemo.dto.SignupRequest;
import com.example.authdemo.model.User;
import com.example.authdemo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")

public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            User user = userService.signup(request.getUsername(), request.getEmail(), request.getPassword());

            // Return JSON response instead of plain text
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully: " + user.getUsername());
            response.put("status", "SUCCESS");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Return JSON error response
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "ERROR");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean success = userService.login(request.getUsername(), request.getPassword());
        if (success) {
            // Return JSON response instead of plain text
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("status", "SUCCESS");

            return ResponseEntity.ok(response);
        } else {
            // Return JSON error response instead of plain text
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password");
            errorResponse.put("status", "ERROR");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}