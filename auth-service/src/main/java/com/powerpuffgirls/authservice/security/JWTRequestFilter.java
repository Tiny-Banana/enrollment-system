package com.powerpuffgirls.authservice.security;

import com.powerpuffgirls.authservice.model.User;
import com.powerpuffgirls.authservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        if (requestPath.equals("/api/user/login") || requestPath.equals("/api/user/register")) {
            // Skip the filter for login and register endpoints
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsername(token);
                int id = jwtUtil.getId(token); // Extract the ID
                String role = jwtUtil.getRole(token); // Extract the role

                // Create a User object to store as the principal
                User user = new User();
                user.setId(id); // Set the extracted ID
                user.setUsername(username); // Set the username
                user.setRole(User.Role.valueOf(role)); // Set the user's role

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);  // Continue with the request
    }

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // No initialization needed
//    }
//
//    @Override
//    public void destroy() {
//        // No cleanup needed
//    }
}
