package com.powerpuffgirls.gradeservice.controller;

import com.powerpuffgirls.gradeservice.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getAllGradesForStudent(@PathVariable int studentId,
                                                    @RequestHeader("Authorization") String authorizationHeader) {
        return gradeService.getGradesByStudentId(studentId, authorizationHeader);
    }
}
