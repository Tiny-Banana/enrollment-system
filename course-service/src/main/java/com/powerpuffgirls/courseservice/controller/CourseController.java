package com.powerpuffgirls.courseservice.controller;

import com.powerpuffgirls.courseservice.model.Course;
import com.powerpuffgirls.courseservice.model.Enrollment;
import com.powerpuffgirls.courseservice.security.JWTUtil;
import com.powerpuffgirls.courseservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    private final JWTUtil jwtUtil;

    @Autowired
    public CourseController(CourseService courseService, JWTUtil jwtUtil) {
        this.courseService = courseService;
        this.jwtUtil = new JWTUtil();
    }

    @GetMapping()
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Course>> getAvailableCourses() {
        List<Course> courses = courseService.getAvailableCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/enrollments/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollments(@PathVariable int courseId) {
        List<Enrollment> enrollments = courseService.getAllEnrollmentsForCourse(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping("/enroll/{courseId}/{studentId}")
    public ResponseEntity<String> enrollStudent(@PathVariable int courseId,
                                                @PathVariable int studentId,
                                                @RequestHeader("Authorization") String authorizationHeader
    ) {
        return courseService.enrollStudentInCourse(courseId, studentId, authorizationHeader);
    }


}