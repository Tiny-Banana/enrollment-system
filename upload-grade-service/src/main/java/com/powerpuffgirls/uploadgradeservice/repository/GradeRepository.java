package com.powerpuffgirls.uploadgradeservice.repository;


import com.powerpuffgirls.uploadgradeservice.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    Optional<Grade> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
}

