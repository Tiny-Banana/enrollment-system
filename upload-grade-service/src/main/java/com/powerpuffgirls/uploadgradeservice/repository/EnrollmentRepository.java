package com.powerpuffgirls.uploadgradeservice.repository;

import com.powerpuffgirls.common.model.Enrollment;
import com.powerpuffgirls.common.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    boolean existsByStudentIdAndCourseId(int studentId, int courseId);
    List<Enrollment> findByCourseId(int courseId);
    @Query("SELECT new com.powerpuffgirls.common.model.Student(u.id, u.name) " +
            "FROM Enrollment e " +
            "JOIN User u ON u.id = e.studentId " +
            "WHERE e.courseId = :courseId")
    List<Student> findStudentsByCourseId(@Param("courseId") int courseId);
}