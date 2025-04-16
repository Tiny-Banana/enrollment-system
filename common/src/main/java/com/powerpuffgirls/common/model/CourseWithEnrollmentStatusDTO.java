package com.powerpuffgirls.common.model;

public class CourseWithEnrollmentStatusDTO {
    private int id;
    private String name;
    private String instructor;
    private String timeslot;
    private int enrolledStudents;
    private int maxStudents;
    private boolean isEnrolled; // Add the calculated field

    public CourseWithEnrollmentStatusDTO(int id, String name, String instructor, String timeslot, int enrolledStudents, int maxStudents, boolean isEnrolled) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
        this.timeslot = timeslot;
        this.enrolledStudents = enrolledStudents;
        this.maxStudents = maxStudents;
        this.isEnrolled = isEnrolled;
    }

    // Getters and setters
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

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }
}