package com.powerpuffgirls.courseservice.service;

import com.powerpuffgirls.courseservice.model.Course;
import com.powerpuffgirls.courseservice.repository.CourseRepository;
import com.powerpuffgirls.courseservice.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final JWTUtil jwtUtil;

    @Autowired
    public CourseService(CourseRepository courseRepository, JWTUtil jwtUtil) {
        this.courseRepository = courseRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findAvailableCourses();
    }
}