package com.school.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private String major;
    private List<ClassSession> enrolledClasses = new ArrayList<>();

    public Student(String id, String name, String major) {
        this.id = id;
        this.name = name;
        this.major = major;
    }

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

    public int getCurrentCredits() {
        int sum = 0;
        for (ClassSession cs : enrolledClasses) {
            if (cs != null && cs.getCourse() != null) {
                sum += cs.getCourse().getCredits();
            }
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", major='" + major + '\'' +
                ", enrolled=" + enrolledClasses.size() +
                '}';
    }
}

