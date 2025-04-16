package com.powerpuffgirls.uploadgradeservice.service;

import com.powerpuffgirls.common.model.Course;
import com.powerpuffgirls.common.model.Student;
import com.powerpuffgirls.uploadgradeservice.model.Grade;
import com.powerpuffgirls.uploadgradeservice.model.GradeRequest;
import com.powerpuffgirls.uploadgradeservice.repository.CourseRepository;
import com.powerpuffgirls.uploadgradeservice.repository.EnrollmentRepository;
import com.powerpuffgirls.uploadgradeservice.repository.GradeRepository;
import com.powerpuffgirls.uploadgradeservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UploadGradeService {
    private final GradeRepository gradeRepository;
    private final JWTUtil jwtUtil;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public UploadGradeService(GradeRepository gradeRepository, JWTUtil jwtUtil, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.gradeRepository = gradeRepository;
        this.jwtUtil = jwtUtil;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public ResponseEntity<String> uploadGrade(GradeRequest gradeRequest, String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        String role = jwtUtil.getRole(token);
        int currentUserId = jwtUtil.getId(token);

        System.out.println("TOken is " + role + " " + currentUserId);

        if (!role.equals("FACULTY") && currentUserId != gradeRequest.getFacultyId()) {
            return ResponseEntity.status(403).body("Access denied: You do not have permission to upload grades.");
        }

        // Check if grade already exists
        Optional<Grade> existingGrade = gradeRepository.findByStudentIdAndCourseId(
                gradeRequest.getStudentId(), gradeRequest.getCourseId());

        if (existingGrade.isPresent()) {
            Grade gradeToUpdate = existingGrade.get();
            gradeToUpdate.setGrade(gradeRequest.getGrade());
            gradeRepository.save(gradeToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body("Grade updated successfully.");
        }

        Grade grade = new Grade();
        grade.setStudentId(gradeRequest.getStudentId());
        grade.setCourseId(gradeRequest.getCourseId());
        grade.setGrade(gradeRequest.getGrade());

        gradeRepository.save(grade);
        return ResponseEntity.status(HttpStatus.OK).body("Grade uploaded successfully");
    }

    public ResponseEntity<?> getHandledCourses(String token, int requestedFacultyId) {
        try {
            // Extract the student ID from the JWT token
            int facultyId = jwtUtil.getId(token);

            System.out.println("From getHandledCourses");

            System.out.println("Extracted facultyId: " + facultyId);
            System.out.println("Requested facultyId: " + requestedFacultyId);

            // Verify that the requestor is trying to access their own grades
            if (facultyId != requestedFacultyId) {
                return ResponseEntity.status(403).body("Access denied: Cannot view grades for other students.");
            }

            System.out.println("Matching faculty ids");

            // Fetch courses for the faculty from the repository
            List<Course> courses = null;
            try {
                courses = courseRepository.findCourseByFacultyId(facultyId);
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

    public ResponseEntity<?> getStudentsForCourse(int courseId) {
        List<Student> students = enrollmentRepository.findStudentsByCourseId(courseId);

        System.out.println("The students for this course are:" + students);
        if (students.isEmpty()) {
            // Respond with 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(students);
    }
}
