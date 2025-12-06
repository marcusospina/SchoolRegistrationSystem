package com.school.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private String major;
    private List<ClassSession> enrolledClasses;

//contructor to initialize fields above
    public Student(String id, String name, String major) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.enrolledClasses = new ArrayList<>();
    }
// getters and setters for all fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public List<ClassSession> getEnrolledClasses() { return enrolledClasses; }
    public void setEnrolledClasses(List<ClassSession> enrolledClasses) {
        this.enrolledClasses = enrolledClasses;
    }
/* method iterates through enrolledClasses and sums the credits of the
associated courses.*/
    public int getCurrentCredits() {
        int sum = 0;
        for (ClassSession s : enrolledClasses) {
            sum += s.getCourse().getCredits();
        }
        return sum;
    }

// toString() for debugging and printing
    public String toString() {
        return id + " - " + name + " (" + major + ")";
}

