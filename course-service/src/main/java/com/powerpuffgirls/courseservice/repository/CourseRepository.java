package com.powerpuffgirls.courseservice.repository;

import com.powerpuffgirls.courseservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
//    List<Course> findByEnrolledStudentsLessThanMaxStudents();
    @Query("SELECT c FROM Course c WHERE c.enrolled_students < c.max_students")
    List<Course> findAvailableCourses();
}