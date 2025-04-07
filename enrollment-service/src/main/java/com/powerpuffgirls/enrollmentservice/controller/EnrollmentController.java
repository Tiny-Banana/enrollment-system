package com.powerpuffgirls.enrollmentservice.controller;

import com.powerpuffgirls.enrollmentservice.model.Enrollment;
import com.powerpuffgirls.enrollmentservice.model.EnrollmentRequest;
import com.powerpuffgirls.enrollmentservice.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping()
    public ResponseEntity<String> enrollStudent(@RequestBody EnrollmentRequest enrollmentRequest,
                                                @RequestHeader("Authorization") String authorizationHeader
    ) {
        return enrollmentService.enrollStudentInCourse(enrollmentRequest, authorizationHeader);
    }
}
