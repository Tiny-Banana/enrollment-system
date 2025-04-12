package com.powerpuffgirls.gradeservice.model;

import java.math.BigDecimal;

public class StudentGradeDTO {
    private BigDecimal grade;
    private String courseName;
    private String studentName;

    // Constructor
    public StudentGradeDTO(BigDecimal grade, String courseName, String studentName) {
        this.grade = grade;
        this.courseName = courseName;
        this.studentName = studentName;
    }

    // Getters and Setters
    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentId(String studentName) {
        this.studentName = studentName;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}