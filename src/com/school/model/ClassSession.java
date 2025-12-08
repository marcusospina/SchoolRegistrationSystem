package com.school.model;

import java.util.ArrayList;
import java.util.List;

public class ClassSession {
    private Course course;
    private Instructor instructor;
    private Classroom classroom;
    private int sectionNumber;
    private int maxCapacity;
    private List<Student> enrolledStudents;

    public ClassSession(Course course, Instructor instructor, Classroom classroom,
                        int sectionNumber, int maxCapacity) {
        this.course = course;
        this.instructor = instructor;
        this.classroom = classroom;
        this.sectionNumber = sectionNumber;
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new ArrayList<>();
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

// %s and %d are j placeholders to format
    public String toString() {
        return String.format("%s Sec-%d | Room: %s | Instructor: %s | Enrolled: %d/%d",
                course.getCourseId(),
                sectionNumber,
                classroom.getRoomNumber(),
                instructor.getName(),
                enrolledStudents.size(),
                maxCapacity);
    }
}

