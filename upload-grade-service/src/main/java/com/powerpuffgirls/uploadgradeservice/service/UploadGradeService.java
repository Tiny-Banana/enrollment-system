package com.powerpuffgirls.uploadgradeservice.service;

import com.powerpuffgirls.uploadgradeservice.model.Grade;
import com.powerpuffgirls.uploadgradeservice.model.GradeRequest;
import com.powerpuffgirls.uploadgradeservice.repository.GradeRepository;
import com.powerpuffgirls.uploadgradeservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UploadGradeService {
    private final GradeRepository gradeRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public UploadGradeService(GradeRepository gradeRepository, JWTUtil jwtUtil) {
        this.gradeRepository = gradeRepository;
        this.jwtUtil = jwtUtil;
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
