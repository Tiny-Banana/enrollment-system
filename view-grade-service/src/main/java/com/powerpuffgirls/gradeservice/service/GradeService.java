package com.powerpuffgirls.gradeservice.service;

import com.powerpuffgirls.gradeservice.model.Course;
import com.powerpuffgirls.gradeservice.model.Grade;
import com.powerpuffgirls.gradeservice.model.GradeCourseDTO;
import com.powerpuffgirls.gradeservice.model.StudentGradeDTO;
import com.powerpuffgirls.gradeservice.repository.CourseRepository;
import com.powerpuffgirls.gradeservice.repository.GradeRepository;
import com.powerpuffgirls.gradeservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final CourseRepository courseRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public GradeService(GradeRepository gradeRepository, CourseRepository courseRepository, JWTUtil jwtUtil) {
        this.gradeRepository = gradeRepository;
        this.courseRepository = courseRepository;
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

            List<GradeCourseDTO> grades = null;
            try {
                grades = gradeRepository.findGradeAndCourseNameByStudentId(studentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return grades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token.");
        }
    }

    public ResponseEntity<?> getGradesForFaculty(String token, Integer requestedFacultyId) {
        try {
            // Extract the student ID from the JWT token
            int facultyId = jwtUtil.getId(token);

            System.out.println("From getGradesForFaculty");
            System.out.println("Extracted facultyId: " + facultyId);
            System.out.println("Requested facultyId: " + requestedFacultyId);

            // Verify that the requestor is trying to access their own grades
            if (facultyId != requestedFacultyId) {
                return ResponseEntity.status(403).body("Access denied: Cannot view grades for other students.");
            }

            System.out.println("Matching fac ids");

            // Fetch all courses handled by the faculty
            List<Course> courses = courseRepository.findCourseByFacultyId(facultyId);

            if (courses.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<Integer> courseIds = courses.stream()
                    .map(Course::getId)
                    .toList();


            try {
                List<StudentGradeDTO> grades = gradeRepository.findGradesByCourseIds(courseIds);

                return grades.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(grades);

            } catch (Exception e) {
                // Log the exception stack trace for debugging
                e.printStackTrace();

                // Return an internal server error response to the client
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching grades.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token.");
        }
    }
}
