package com.powerpuffgirls.courseservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "student_course")  // Map to the existing student_course table
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "student_id", nullable = false)
    private int studentId;  // Reference to the Student ID, without needing a full Student entity

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;  // Reference to the Course entity for course-specific data

    public Enrollment() {}

    public Enrollment(int studentId, Course course) {
        this.studentId = studentId;
        this.course = course;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}