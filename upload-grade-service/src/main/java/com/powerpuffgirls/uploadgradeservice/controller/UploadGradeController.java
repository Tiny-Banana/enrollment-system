package com.powerpuffgirls.uploadgradeservice.controller;

import com.powerpuffgirls.uploadgradeservice.model.GradeRequest;
import com.powerpuffgirls.uploadgradeservice.service.UploadGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/grades")
public class UploadGradeController {
    private final UploadGradeService uploadGradeService;

    @Autowired
    public UploadGradeController(UploadGradeService uploadGradeService) {
        this.uploadGradeService = uploadGradeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadGrade(@RequestBody GradeRequest gradeRequest,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        return uploadGradeService.uploadGrade(gradeRequest,  authorizationHeader);
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

            System.out.println("Returned" + uploadGradeService.getHandledCourses(jwt, requestedFacultyId));
            // Delegate fetching grades to the service
            return uploadGradeService.getHandledCourses(jwt, requestedFacultyId);

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
                System.out.println("Returned" + uploadGradeService.getStudentsForCourse(courseId));
                // Delegate fetching grades to the service
                uploadGradeService.getStudentsForCourse(courseId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return uploadGradeService.getStudentsForCourse(courseId);


        } catch (Exception e) {
            // Handle invalid or expired token
            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token.");
        }
    }
}
