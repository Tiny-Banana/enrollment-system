package com.powerpuffgirls.viewservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    //authentication node
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String viewDashboard() {
        return "dashboard";
    }

    //=================

    //course node
    @GetMapping("/courses")
    public String viewCourses() { return "courses"; }

    //enrollment node
    @GetMapping("/enrollments")
    public String viewEnrollments() { return "enrollments"; }

    //enrollment node
    @GetMapping("/enroll")
    public String enroll() { return "enroll"; }

    //grades node
    @GetMapping("/grades")
    public String viewGrades() { return "grades"; }

    @GetMapping("/faculty/viewfac")
    public String facultyViewGrades() {
        return "faculty_grades";
    }

    @GetMapping("/faculty/upload")
    public String facultyUploadGrades() {
        return "upload_grade";
    }
}
