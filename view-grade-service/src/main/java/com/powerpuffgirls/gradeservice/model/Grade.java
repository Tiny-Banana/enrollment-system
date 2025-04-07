package com.powerpuffgirls.gradeservice.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int studentId;

    @Column(nullable = false)
    private int courseId;

    @Column(nullable = false)
    private BigDecimal grade;

    public Grade() {}

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
