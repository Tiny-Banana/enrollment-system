package com.powerpuffgirls.gradeservice.model;

import java.math.BigDecimal;

public class GradeCourseDTO {
    private BigDecimal grade;
    private String courseName;

    // Constructor
    public GradeCourseDTO(BigDecimal grade, String courseName) {
        this.grade = grade;
        this.courseName = courseName;
    }

    // Getters and Setters
    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}