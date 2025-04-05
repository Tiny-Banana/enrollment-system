package com.powerpuffgirls.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTRequestFilter jwtFilter;

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http.csrf(csrf -> csrf.disable()) // Disable CSRF for the app
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()  // Permit login and register requests
//                        .anyRequest().authenticated() // Require authentication for all other endpoints
//                )
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
//                .build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()) // Allow all requests without authentication
                .build();
    }



}