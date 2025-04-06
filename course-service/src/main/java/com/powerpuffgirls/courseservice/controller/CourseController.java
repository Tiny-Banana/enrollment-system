package com.powerpuffgirls.courseservice.controller;

import com.powerpuffgirls.courseservice.model.Course;
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

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
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

    @PostMapping("/enroll/{courseId}/{studentId}")
    public ResponseEntity<String> enrollStudent(@PathVariable int courseId,
                                                @PathVariable int studentId,
                                                @RequestHeader("Authorization") String authorizationHeader
    ) {
        // Construct HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        return courseService.enrollStudentInCourse(courseId, studentId, headers);
    }
}