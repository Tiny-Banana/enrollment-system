package com.powerpuffgirls.enrollmentservice.service;

import com.powerpuffgirls.enrollmentservice.model.Enrollment;
import com.powerpuffgirls.enrollmentservice.model.EnrollmentRequest;
import com.powerpuffgirls.enrollmentservice.repository.EnrollmentRepository;
import com.powerpuffgirls.enrollmentservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, JWTUtil jwtUtil) {
        this.enrollmentRepository = enrollmentRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<Enrollment> getAllEnrollmentsForCourse(int courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public ResponseEntity<String> enrollStudentInCourse(EnrollmentRequest enrollmentRequest, String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        String role = jwtUtil.getRole(token);
        int currentUserId = jwtUtil.getId(token);

        if (!role.equals("student") && currentUserId != enrollmentRequest.getStudentId()) {
            return ResponseEntity.status(403).body("Access denied: You do not have permission to enroll.");
        }

        // Check if the student is already enrolled
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(enrollmentRequest.getStudentId(), enrollmentRequest.getCourseId());
        if (alreadyEnrolled) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Student is already enrolled in this course");
        }

        // Enroll the student
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(enrollmentRequest.getStudentId());
        enrollment.setCourseId(enrollmentRequest.getCourseId());

        enrollmentRepository.save(enrollment);

        return ResponseEntity.status(HttpStatus.OK).body("Student enrolled successfully");
    }
}
