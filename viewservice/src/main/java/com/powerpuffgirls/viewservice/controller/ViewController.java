package com.powerpuffgirls.viewservice.controller;

import com.powerpuffgirls.courseservice.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String COURSE_CONT_URL = "http://localhost:8081/api/courses";

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
    public String viewCourses(Model model) {

        String viewCoursesURL = COURSE_CONT_URL + "/available";
        try {
            ResponseEntity<List> response = restTemplate.exchange(viewCoursesURL, HttpMethod.GET, null, List.class);

            List<Course> courses = response.getBody();
            model.addAttribute("courses", courses);

        } catch (Exception e) {
            System.err.println("Error fetching courses: " + e.getMessage());
            model.addAttribute("errorMessage", "Unable to fetch courses at the moment. Please try again later.");
        }

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
