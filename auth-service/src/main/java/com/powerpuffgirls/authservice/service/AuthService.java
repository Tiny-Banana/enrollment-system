package com.powerpuffgirls.authservice.service;

import com.powerpuffgirls.authservice.model.LoginRequest;
import com.powerpuffgirls.authservice.model.User;
import com.powerpuffgirls.authservice.repository.UserRepository;
import com.powerpuffgirls.authservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(User user) {
        try {
            // Check if the username is already taken
            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest().body("Username already taken");
            }

            // Encrypt the password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save the user
            userRepository.save(user);

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }

    public ResponseEntity<?> login(LoginRequest request) {
        try {
            // Find the user by username
            Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            User user = userOptional.get();

            // Check if the password matches
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            // Generate a JWT token for the user
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            return ResponseEntity.ok().body("Bearer " + token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }

    public ResponseEntity<?> logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return ResponseEntity.ok().body("Logged out successfully");
        } else {
            return ResponseEntity.status(400).body("No token found to logout");
        }
    }
}
