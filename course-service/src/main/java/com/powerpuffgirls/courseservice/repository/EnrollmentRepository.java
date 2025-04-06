package com.powerpuffgirls.courseservice.repository;

import com.powerpuffgirls.courseservice.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    boolean existsByStudentIdAndCourseId(int studentId, int courseId);
}
