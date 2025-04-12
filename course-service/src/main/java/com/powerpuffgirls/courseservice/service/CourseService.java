package com.powerpuffgirls.courseservice.service;

import com.powerpuffgirls.courseservice.model.Course;
import com.powerpuffgirls.courseservice.model.CourseWithEnrollmentStatusDTO;
import com.powerpuffgirls.courseservice.repository.CourseRepository;
import com.powerpuffgirls.courseservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public CourseService(CourseRepository courseRepository, JWTUtil jwtUtil) {
        this.courseRepository = courseRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }

    public ResponseEntity<?> getAvailableCoursesWithEnrollmentStatus(String token, int requestedStudentId) {
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

            // Fetch courses for the student from the repository

            List<CourseWithEnrollmentStatusDTO> courses = null;
            try {
                courses = courseRepository.findAvailableCoursesWithEnrollmentFlag(studentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Return the grades if found, or 204 No Content if no grades
            return courses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(courses);
        } catch (Exception e) {
            // Handle exceptions like invalid or expired tokens
            return ResponseEntity.status(401).body("Invalid or expired token.");
        }
    }

}

