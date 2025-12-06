package com.school.model;

import java.util.ArrayList;
import java.util.List;

public class Instructor {
    private String id;
    private String name;
    private List<String> qualifiedCourses;
    private List<ClassSession> teachingAssignment;

    public Instructor(String id, String name, List<String> qualifiedCourses) {
        this.id = id;
        this.name = name;
        this.qualifiedCourses = qualifiedCourses;
        this.teachingAssignment = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getQualifiedCourses() { return qualifiedCourses; }
    public void setQualifiedCourses(List<String> qualifiedCourses) {
        this.qualifiedCourses = qualifiedCourses;
    }

    public List<ClassSession> getTeachingAssignment() { return teachingAssignment; }
    public void setTeachingAssignment(List<ClassSession> teachingAssignment) {
        this.teachingAssignment = teachingAssignment;
    }

    public boolean canTeach(Course c) {
        return qualifiedCourses.contains(c.getCourseId());
    }

    public int getCurrentLoad() {
        int load = 0;
        for (ClassSession s : teachingAssignment) {
            load += s.getCourse().getCredits();
        }
        return load;
    }


    public String toString() {
        return id + " - " + name + " | Qualified: " + qualifiedCourses;
    }
}

