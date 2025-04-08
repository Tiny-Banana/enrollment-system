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
public class UserService {
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> findUser(int id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity.ok(userOptional.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }
    
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

            String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());

            return ResponseEntity.status(HttpStatus.CREATED).body(token);
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            User user = userOptional.get();

            // Check if the password matches
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            System.out.println("All good.. Generating token..");

            // Generate a JWT token for the user
            String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());

            System.out.println("Token generated: " + token);

            return ResponseEntity.ok().body(token);
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
