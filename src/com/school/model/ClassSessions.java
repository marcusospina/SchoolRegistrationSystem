package com.school.model;

import java.util.ArrayList;
import java.util.List;

public class ClassSession {
    private Course course;
    private Instructor instructor;
    private Classroom classroom;
    private int sectionNumber;
    private int maxCapacity;
    private List<Student> enrolledStudents = new ArrayList<>();

    public ClassSession(Course course, Instructor instructor, Classroom classroom,
                        int sectionNumber, int maxCapacity) {
        this.course = course;
        this.instructor = instructor;
        this.classroom = classroom;
        this.sectionNumber = sectionNumber;
        this.maxCapacity = maxCapacity;
    }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }

    public Classroom getClassroom() { return classroom; }
    public void setClassroom(Classroom classroom) { this.classroom = classroom; }

    public int getSectionNumber() { return sectionNumber; }
    public void setSectionNumber(int sectionNumber) { this.sectionNumber = sectionNumber; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public boolean isFull() {
        return enrolledStudents.size() >= maxCapacity;
    }

    @Override
    public String toString() {
        return "ClassSession{" +
                "course=" + course +
                ", instructor=" + (instructor != null ? instructor.getName() : "null") +
                ", classroom=" + (classroom != null ? classroom.getRoomNumber() : "null") +
                ", sectionNumber=" + sectionNumber +
                ", maxCapacity=" + maxCapacity +
                ", enrolled=" + enrolledStudents.size() +
                '}';
    }
}

