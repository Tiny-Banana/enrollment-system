package com.powerpuffgirls.enrollmentservice.service;

import com.powerpuffgirls.common.model.Course;
import com.powerpuffgirls.enrollmentservice.model.CourseWithEnrollmentStatusDTO;
import com.powerpuffgirls.enrollmentservice.model.Enrollment;
import com.powerpuffgirls.enrollmentservice.model.EnrollmentRequest;
import com.powerpuffgirls.enrollmentservice.repository.CourseRepository;
import com.powerpuffgirls.enrollmentservice.repository.EnrollmentRepository;
import com.powerpuffgirls.enrollmentservice.repository.UserRepository;
import com.powerpuffgirls.enrollmentservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             UserRepository userRepository,
                             CourseRepository courseRepository,
                             JWTUtil jwtUtil) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<Enrollment> getAllEnrollmentsForCourse(int courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public ResponseEntity<String> enrollStudentInCourse(EnrollmentRequest enrollmentRequest, String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        String role = jwtUtil.getRole(token);
        int currentUserId = jwtUtil.getId(token);

        if (!userRepository.existsById(currentUserId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User does not exist.");
        }

        if (!role.equals("student") && currentUserId != enrollmentRequest.getStudentId()) {
            return ResponseEntity.status(403).body("Access denied: You do not have permission to enroll.");
        }

        Course course = courseRepository.findById(enrollmentRequest.getCourseId()).orElse(null);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Course does not exist");
        } else if (course.getEnrolledStudents() >= course.getMaxStudents()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Course is full");
        }

        boolean alreadyEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(enrollmentRequest.getStudentId(), enrollmentRequest.getCourseId());
        if (alreadyEnrolled) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Student is already enrolled in this course");
        }

        // Enroll the student
        Enrollment enrollment = new Enrollment(
                enrollmentRequest.getStudentId(),
                enrollmentRequest.getCourseId());
        enrollmentRepository.save(enrollment);
        course.setEnrolledStudents(course.getEnrolledStudents() + 1);
        courseRepository.save(course);

//        enrollmentPublisher.publishUserEnrollment(enrollmentRequest);
//        enrollmentPublisher.publishCourseEnrollment(enrollmentRequest);

        return ResponseEntity.status(HttpStatus.OK).body("Student enrolled.");
    }

    public ResponseEntity<?> getEnrolledCourses(String token, int requestedStudentId) {
        try {
            // Extract the student ID from the JWT token
            int studentId = jwtUtil.getId(token);

            System.out.println("Extracted studentId: " + studentId);
            System.out.println("Requested studentId: " + requestedStudentId);

            // Verify that the requestor is trying to access their own grades
            if (studentId != requestedStudentId) {
                return ResponseEntity.status(403).body("Access denied: Cannot view grades for other students.");
            }

            System.out.println("Matching student ids");

            // Fetch courses for the student from the repository

            List<CourseWithEnrollmentStatusDTO> courses = null;
            try {
                courses = courseRepository.findCoursesWithEnrollmentStatus(studentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Return the grades if found, or 204 No Content if no grades
            return courses.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(courses);
        } catch (Exception e) {
            // Handle exceptions like invalid or expired tokens
            return ResponseEntity.status(401).body("Invalid or expired token.");
        }
    }

//    @RabbitListener(queues = "userExistenceResultQueue")
//    public void handleEnrollmentResults(UserExistenceResult result) {
//        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(result.getUserId(), result.getCourseId());
//        if (enrollment != null) {
//            if ("FAILED".equalsIgnoreCase(result.getStatus())) {
//                enrollment.setStatus(result.getStatus());
//                enrollmentRepository.save(enrollment);
//            }
//
//            System.out.println(String.format("Received enrollment result: %s with the reason: %s for student %d in course %d",
//                    result.getStatus(),
//                    result.getReason(),
//                    result.getUserId(),
//                    result.getCourseId()));
//        }
//    }
//
//    @RabbitListener(queues = "enrollmentResultQueue")
//    public void handleEnrollmentResults(EnrollmentResult result) {
//        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(result.getStudentId(), result.getCourseId());
//
//        if (enrollment != null) {
//            if ("FAILED".equalsIgnoreCase(result.getStatus())) {
//                enrollmentRepository.delete(enrollment);
//                System.out.println("Enrollment failed. Deleted record.");
//            } else {
//                enrollment.setStatus(result.getStatus());
//                enrollmentRepository.save(enrollment);
//                System.out.println("Enrollment succeeded. Updated record.");
//            }
//
//            System.out.println(String.format("Received enrollment result: %s with the reason: %s for student %d in course %d",
//                    result.getStatus(),
//                    result.getReason(),
//                    result.getStudentId(),
//                    result.getCourseId()));
//        } else {
//            System.out.println("No enrollment found to update.");
//        }
//    }
}
