package com.powerpuffgirls.gradeservice.repository;

import com.powerpuffgirls.gradeservice.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    List<Grade> findByStudentId(Integer studentId);
    Optional<Grade> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
