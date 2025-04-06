package com.powerpuffgirls.courseservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String instructor;

    @Column(nullable = false)
    private int units;

    @Column(nullable = false)
    private String timeslot;

    @Column(nullable = false)
    private int semester;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int enrolled_students; // Number of students currently enrolled

    @Column(nullable = false)
    private int max_students; // Maximum number of students allowed

//    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference

//    private Set<Enrollment> enrollments = new HashSet<>(); // Direct reference to Enrollment

//    public Set<Enrollment> getEnrollments() {
//        return enrollments;
//    }
//
//    public void setEnrollments(Set<Enrollment> enrollments) {
//        this.enrollments = enrollments;
//    }

    public int getEnrolledStudents() {
        return enrolled_students;
    }

    public void setEnrolledStudents(int enrolled_students) {
        this.enrolled_students = enrolled_students;
    }

    public int getMaxStudents() {
        return max_students;
    }

    public void setMaxStudents(int max_students) {
        this.max_students = max_students;
    }

    public Course() {
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
