package com.powerpuffgirls.courseservice.service;

import com.powerpuffgirls.courseservice.model.Course;
import com.powerpuffgirls.courseservice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses(); // Fetches all courses
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
