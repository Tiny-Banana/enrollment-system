package com.powerpuffgirls.gradeservice.service;

import com.powerpuffgirls.gradeservice.model.Grade;
import com.powerpuffgirls.gradeservice.model.GradeRequest;
import com.powerpuffgirls.gradeservice.repository.GradeRepository;
import com.powerpuffgirls.gradeservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public ResponseEntity<String> uploadGrade(GradeRequest gradeRequest, String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        String role = jwtUtil.getRole(token);
        int currentUserId = jwtUtil.getId(token);

        if (!role.equals("faculty") && currentUserId != gradeRequest.getFacultyId()) {
            return ResponseEntity.status(403).body("Access denied: You do not have permission to upload grades.");
        }

        // Check if grade already exists
        Optional<Grade> existingGrade = gradeRepository.findByStudentIdAndCourseId(
                gradeRequest.getStudentId(), gradeRequest.getCourseId());

        if (existingGrade.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Grade already uploaded for this student in this course.");
        }

        Grade grade = new Grade();
        grade.setStudentId(gradeRequest.getStudentId());
        grade.setCourseId(gradeRequest.getCourseId());
        grade.setGrade(gradeRequest.getGrade());

        gradeRepository.save(grade);
        return ResponseEntity.status(HttpStatus.OK).body("Grade uploaded successfully");
    }

}
