package com.powerpuffgirls.uploadgradeservice.controller;

import com.powerpuffgirls.uploadgradeservice.model.GradeRequest;
import com.powerpuffgirls.uploadgradeservice.service.UploadGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
