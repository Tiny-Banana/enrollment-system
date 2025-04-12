package com.powerpuffgirls.gradeservice.service;

import com.powerpuffgirls.gradeservice.model.Grade;
import com.powerpuffgirls.gradeservice.model.GradeCourseDTO;
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

    public ResponseEntity<?> getGradesByStudentId(String token, Integer requestedStudentId) {
        try {
            // Extract the student ID from the JWT token
            int studentId = jwtUtil.getId(token);

            System.out.println("Extracted studentId: " + studentId);
            System.out.println("Requested studentId: " + requestedStudentId);

            // Verify that the requestor is trying to access their own grades
            if (studentId != requestedStudentId) {
                return ResponseEntity.status(403).body("Access denied: Cannot view grades for other students.");
            }

            System.out.println("Matching student ids");

            // Fetch grades for the student from the repository

            List<GradeCourseDTO> grades = null;
            try {
                grades = gradeRepository.findGradeAndCourseNameByStudentId(studentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Return the grades if found, or 204 No Content if no grades
            return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
        } catch (Exception e) {
            // Handle exceptions like invalid or expired tokens
            return ResponseEntity.status(401).body("Invalid or expired token.");
        }
    }
}
