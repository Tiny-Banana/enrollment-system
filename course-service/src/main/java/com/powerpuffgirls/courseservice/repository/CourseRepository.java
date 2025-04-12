package com.powerpuffgirls.courseservice.repository;

import com.powerpuffgirls.courseservice.model.Course;
import com.powerpuffgirls.courseservice.model.CourseWithEnrollmentStatusDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query(value = "SELECT new com.powerpuffgirls.courseservice.model.CourseWithEnrollmentStatusDTO(" +
            "c.id, c.name, c.instructor, c.timeslot, c.enrolled_students, c.max_students, " +
            "CASE WHEN e.studentId IS NOT NULL THEN true ELSE false END) " +
            "FROM Course c " +
            "LEFT JOIN Enrollment e ON c.id = e.courseId AND e.studentId = :studentId " +
            "WHERE c.enrolled_students < c.max_students")
    List<CourseWithEnrollmentStatusDTO> findAvailableCoursesWithEnrollmentFlag(@Param("studentId") Integer studentId);

    @Query("SELECT c FROM Course c WHERE c.enrolled_students < c.max_students")
    List<Course> findAvailableCourses();

}