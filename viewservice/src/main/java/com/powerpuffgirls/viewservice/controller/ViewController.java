package com.powerpuffgirls.viewservice.controller;

import com.powerpuffgirls.authservice.model.LoginRequest;
import com.powerpuffgirls.authservice.model.User;
import com.powerpuffgirls.courseservice.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String AUTH_CONT_URL = "http://localhost:8083/api/user";
    private static final String COURSE_CONT_URL = "http://localhost:8081/api/courses";

    //authentication node
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, Model model) {

        String loginURL = AUTH_CONT_URL + "/login";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(loginURL, HttpMethod.POST, request, String.class);

            String token = response.getBody();

            if (response.getStatusCode() == HttpStatus.OK && token != null) {
                model.addAttribute("token", token);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("errorMessage", "Invalid credentials, please try again.");
                return "login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while logging in.");
            return "login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {

        String registerURL = AUTH_CONT_URL + "/register";

        System.out.println("User: " + user.getName() + " " + user.getUsername() + " " + user.getPassword() + " " + user.getRole());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<User> request = new HttpEntity<>(user, headers);

            ResponseEntity<String> response = restTemplate.exchange(registerURL, HttpMethod.POST, request, String.class);
            String token = response.getBody();

            if (response.getStatusCode() == HttpStatus.CREATED && token != null) {
                System.out.println("Token " + token);
                model.addAttribute("token", token);
            } else {
                model.addAttribute("error", "Registration failed: " + response.getBody());
            }

            return "register";

        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong: " + e.getMessage());
            System.out.println(e.getMessage());
            return "register";
        }
    }

    //================
    @GetMapping("/dashboard")
    public String viewDashboard() {
        return "dashboard";
    }
    //=================

    //course node
    @GetMapping("/courses")
    public String viewCourses(Model model) {

        String viewCoursesURL = COURSE_CONT_URL;
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

    //enrollment node?
    @GetMapping("/enrollments")
    public String viewEnrollments() {
        //@RequestParam("courseId") int courseId, Model model
        //Course course = courseService.getCourseById(courseId);

        //model.addAttribute("course", course);       // Add course info

        return "enrollments";
    }

    //enrollment node
    @GetMapping("/enroll")
    public String enroll() {
        //model.addAttribute("courseId", courseId);
        return "enroll";
    }

    //grades node

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
