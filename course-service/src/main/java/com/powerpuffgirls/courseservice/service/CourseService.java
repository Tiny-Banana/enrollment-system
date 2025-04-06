package com.powerpuffgirls.courseservice.service;

import com.powerpuffgirls.courseservice.model.Course;
import com.powerpuffgirls.courseservice.model.Enrollment;
import com.powerpuffgirls.courseservice.model.StudentDTO;
import com.powerpuffgirls.courseservice.repository.CourseRepository;
import com.powerpuffgirls.courseservice.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, RestTemplate restTemplate) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.restTemplate = restTemplate;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }

    public StudentDTO getStudentInfo(int studentId, HttpHeaders headers) {
        String url = "http://localhost:8080/api/user/" + studentId;

        try {
            // Create an HttpEntity using the provided headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make the GET request with the headers
            ResponseEntity<StudentDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, StudentDTO.class);

            return response.getBody();
        } catch (Exception e) {
            // If the API call fails, log the error and return a fallback response
            System.err.println("Failed to fetch student details from AuthService: " + e.getMessage());
            return null; // Return null or a default fallback
        }
    }

    public ResponseEntity<String> enrollStudentInCourse(int courseId, int studentId, HttpHeaders headers
    ) {
        // Retrieve course
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        Course course = courseOptional.get();

        // Check course capacity
        if (course.getEnrolledStudents() >= course.getMaxStudents()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course is already full");
        }

        // Check if the student is already enrolled
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
        if (alreadyEnrolled) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Student is already enrolled in this course");
        }

        // Verify that the student exists via AuthService
        StudentDTO student = getStudentInfo(studentId, headers);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }

        // Enroll the student
        Enrollment enrollment = new Enrollment(studentId, course);
        enrollmentRepository.save(enrollment);

        // Update and save course
        course.setEnrolledStudents(course.getEnrolledStudents() + 1);
        courseRepository.save(course);

        return ResponseEntity.status(HttpStatus.OK).body("Student enrolled successfully");
    }
}