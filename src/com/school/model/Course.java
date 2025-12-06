package com.school.model;

public final class Course {
    private final String courseId;
    private final String name;
    private final int credits;

    public Course(String courseId, String name, int credits) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
    }
//no setters because immutable representation
    public String getCourseId() { return courseId; }
    public String getName() { return name; }
    public int getCredits() { return credits; }


    public String toString() {
        return courseId + " - " + name + " (" + credits + " credits)";
    }
}

