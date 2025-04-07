package com.powerpuffgirls.viewservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

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

    @GetMapping("/courses")
    public String viewCourses() {
        return "courses";
    }

    @GetMapping("/enrollments")
    public String viewEnrollments() {
        //@RequestParam("courseId") int courseId, Model model
        //Course course = courseService.getCourseById(courseId);

        //model.addAttribute("course", course);       // Add course info

        return "enrollments";
    }


    @GetMapping("/enroll")
    public String enroll() {
        //model.addAttribute("courseId", courseId);
        return "enroll";
    }

    @GetMapping("/grades")
    public String viewGrades() {
        //@RequestParam("studentId") int studentId, Model model
        //model.addAttribute("studentId", studentId);
        return "grades";
    }

    @GetMapping("/faculty/grades")
    public String facultyViewGrades() {
        return "faculty_grades";
    }

    @GetMapping("/faculty/upload")
    public String facultyUploadGrades() {
        return "upload_grade";
    }
}
