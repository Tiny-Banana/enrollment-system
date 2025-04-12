package com.powerpuffgirls.enrollmentservice.repository;

import com.powerpuffgirls.common.model.Course;
import com.powerpuffgirls.enrollmentservice.model.CourseWithEnrollmentStatusDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    //    List<Course> findByEnrolledStudentsLessThanMaxStudents();
    @Query("SELECT c FROM Course c WHERE c.enrolled_students < c.max_students")
    List<Course> findAvailableCourses();


    @Query(value = "SELECT new com.powerpuffgirls.enrollmentservice.model.CourseWithEnrollmentStatusDTO(" +
            "c.id, c.name, c.instructor, c.timeslot, c.enrolled_students, c.max_students, " +
            "CASE WHEN e.studentId IS NOT NULL THEN true ELSE false END) " +
            "FROM Course c " +
            "LEFT JOIN Enrollment e ON c.id = e.courseId AND e.studentId = :studentId ")
    List<CourseWithEnrollmentStatusDTO> findCoursesWithEnrollmentStatus(@Param("studentId") Integer studentId);

}