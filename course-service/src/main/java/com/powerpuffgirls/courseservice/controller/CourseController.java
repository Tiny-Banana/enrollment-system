package com.powerpuffgirls.courseservice.controller;

import com.powerpuffgirls.courseservice.model.Course;
import com.powerpuffgirls.courseservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @CrossOrigin
    @GetMapping("/available")
    public ResponseEntity<List<Course>> getAvailableCourses() {
        List<Course> courses = courseService.getAvailableCourses();
        return ResponseEntity.ok(courses);
    }

    @CrossOrigin
    @PostMapping("/availabletoenroll")
    public ResponseEntity<?> getAvailableCoursesWithEnrollmentStatus(@RequestHeader("Authorization") String token,
                                                                     @RequestBody Map<String, Integer> requestBody) {

        System.out.println("Request Body: " + requestBody);
        System.out.println("Token: " + token);

        try {
            // Extract student ID from the token
            String jwt = token.replace("Bearer ", "");

            Integer requestedStudentId = requestBody.get("studentId");

            System.out.println("Returned" + courseService.getAvailableCoursesWithEnrollmentStatus(jwt, requestedStudentId));
            // Delegate fetching grades to the service
            return courseService.getAvailableCoursesWithEnrollmentStatus(jwt, requestedStudentId);

        } catch (Exception e) {
            // Handle invalid or expired token
            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token.");
        }

    }

    @CrossOrigin
    @PostMapping("/handled")
    public ResponseEntity<?> getHandledCourses(@RequestHeader("Authorization") String token,
                                               @RequestBody Map<String, Integer> requestBody) {

        System.out.println("Request Body: " + requestBody);
        System.out.println("Token: " + token);

        try {
            // Extract student ID from the token
            String jwt = token.replace("Bearer ", "");

            Integer requestedFacultyId = requestBody.get("facultyId");

            System.out.println("Returned" + courseService.getHandledCourses(jwt, requestedFacultyId));
            // Delegate fetching grades to the service
            return courseService.getHandledCourses(jwt, requestedFacultyId);

        } catch (Exception e) {
            // Handle invalid or expired token
            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token.");
        }

    }
}