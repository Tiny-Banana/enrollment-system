package com.powerpuffgirls.gradeservice.controller;

import com.powerpuffgirls.gradeservice.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/view")
    public ResponseEntity<?> getAllGradesForStudent(@RequestHeader("Authorization") String token,
                                                    @RequestBody Map<String, Integer> requestBody){

        System.out.println("Request Body: " + requestBody);
        System.out.println("Token: " + token);

        try {
            // Extract student ID from the token
            String jwt = token.replace("Bearer ", "");

            Integer requestedStudentId = requestBody.get("studentId");

            System.out.println("Returned" + gradeService.getGradesByStudentId(jwt, requestedStudentId));
            // Delegate fetching grades to the service
            return gradeService.getGradesByStudentId(jwt, requestedStudentId);

        } catch (Exception e) {
            // Handle invalid or expired token
            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token.");
        }
    }

    @PostMapping("/viewfac")
    public ResponseEntity<?> getGradesForFaculty
            (@RequestHeader("Authorization") String token,
                                                    @RequestBody Map<String, Integer> requestBody){

        System.out.println("From viewFac, getGradesForFaculty");

        System.out.println("Request Body: " + requestBody);
        System.out.println("Token: " + token);

        try {
            // Extract student ID from the token
            String jwt = token.replace("Bearer ", "");

            Integer requestedFacultyId = requestBody.get("facultyId");

            System.out.println("Returned" + gradeService.getGradesForFaculty(jwt, requestedFacultyId));
            // Delegate fetching grades to the service
            return gradeService.getGradesForFaculty(jwt, requestedFacultyId);

        } catch (Exception e) {
            // Handle invalid or expired token
            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token.");
        }
    }
}
