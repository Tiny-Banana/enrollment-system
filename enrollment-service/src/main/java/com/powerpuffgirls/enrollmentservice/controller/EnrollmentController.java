package com.powerpuffgirls.enrollmentservice.controller;

import com.powerpuffgirls.enrollmentservice.model.Enrollment;
import com.powerpuffgirls.enrollmentservice.model.EnrollmentRequest;
import com.powerpuffgirls.enrollmentservice.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enroll")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/enrollments/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollments(@PathVariable int courseId) {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollmentsForCourse(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollStudent(@RequestBody EnrollmentRequest enrollmentRequest,
                                                @RequestHeader("Authorization") String authorizationHeader
    ) {
        return enrollmentService.enrollStudentInCourse(enrollmentRequest, authorizationHeader);
    }

    @CrossOrigin
    @PostMapping("/enrolled")
    public ResponseEntity<?> getEnrolledCourses(@RequestHeader("Authorization") String token,
                                                @RequestBody Map<String, Integer> requestBody) {

        System.out.println("Request Body: " + requestBody);
        System.out.println("Token: " + token);

        try {
            // Extract student ID from the token
            String jwt = token.replace("Bearer ", "");

            Integer requestedStudentId = requestBody.get("studentId");

            System.out.println("Returned" + enrollmentService.getEnrolledCourses(jwt, requestedStudentId));
            // Delegate fetching grades to the service
            return enrollmentService.getEnrolledCourses(jwt, requestedStudentId);

        } catch (Exception e) {
            // Handle invalid or expired token
            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token.");
        }

    }

    @CrossOrigin
    @PostMapping("/students")
    public ResponseEntity<?> getStudentsInCourse(@RequestBody Map<String, Integer> requestBody) {

        System.out.println("Request Body: " + requestBody);

        try {

            Integer courseId = requestBody.get("courseId");

            try {
                System.out.println("Returned" + enrollmentService.getStudentsForCourse(courseId));
                // Delegate fetching grades to the service
                enrollmentService.getStudentsForCourse(courseId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return enrollmentService.getStudentsForCourse(courseId);


        } catch (Exception e) {
            // Handle invalid or expired token
            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token.");
        }

    }
}
