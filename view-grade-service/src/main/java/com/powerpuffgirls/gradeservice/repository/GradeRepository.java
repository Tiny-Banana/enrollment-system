package com.powerpuffgirls.gradeservice.repository;

import com.powerpuffgirls.gradeservice.model.Grade;
import com.powerpuffgirls.gradeservice.model.GradeCourseDTO;
import com.powerpuffgirls.gradeservice.model.StudentGradeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    @Query(value = "SELECT g.grade AS grade, c.name AS courseName " +
            "FROM grade g " +
            "JOIN course c ON g.course_id = c.id " +
            "WHERE g.student_id = :studentId", nativeQuery = true)
    List<GradeCourseDTO> findGradeAndCourseNameByStudentId(@Param("studentId") Integer studentId);

    @Query(value = "SELECT g.grade AS grade, c.name AS courseName, s.name AS studentName " +
            "FROM grade g " +
            "JOIN course c ON g.course_id = c.id " +
            "JOIN User s ON g.student_id = s.id " +
            "WHERE g.course_id IN :courseIds", nativeQuery = true)
    List<StudentGradeDTO> findGradesByCourseIds(@Param("courseIds") List<Integer> courseIds);
}
