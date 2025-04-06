package com.powerpuffgirls.authservice.controller;

import com.powerpuffgirls.authservice.model.LoginRequest;
import com.powerpuffgirls.authservice.model.User;
import com.powerpuffgirls.authservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return userService.register(user);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return userService.logout(token);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        return userService.findUser(id);
    }
}