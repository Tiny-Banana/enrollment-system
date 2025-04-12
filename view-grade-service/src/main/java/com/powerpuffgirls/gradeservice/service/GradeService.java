package com.powerpuffgirls.gradeservice.service;

import com.powerpuffgirls.gradeservice.model.Grade;
import com.powerpuffgirls.gradeservice.repository.GradeRepository;
import com.powerpuffgirls.gradeservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public GradeService(GradeRepository gradeRepository, JWTUtil jwtUtil) {
        this.gradeRepository = gradeRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> getGradesByStudentId(Integer studentId, String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        String role = jwtUtil.getRole(token);
        int currentUserId = jwtUtil.getId(token);

        // Check if the user has permission to access the grades
        if (!role.equals("student") && currentUserId != studentId) {
            return ResponseEntity.status(403).body("Access denied: You do not have permission to view these grades.");
        }

        // Fetch grades from the repository
        List<Grade> grades = gradeRepository.findByStudentId(studentId);

        // Return grades or 204 No Content if no grades are found
        return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
    }
}
