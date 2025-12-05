package com.school.model;

import java.util.ArrayList;
import java.util.List;

public class Instructor {
    private String id;
    private String name;
    private List<String> qualifiedCourses = new ArrayList<>();
    private List<ClassSession> teachingAssignment = new ArrayList<>();

    public Instructor(String id, String name, List<String> qualifiedCourses) {
        this.id = id;
        this.name = name;
        if (qualifiedCourses != null) this.qualifiedCourses = qualifiedCourses;
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
        if (c == null) return false;
        return qualifiedCourses.contains(c.getCourseId());
    }

    public int getCurrentLoad() {
        int sum = 0;
        for (ClassSession cs : teachingAssignment) {
            if (cs != null && cs.getCourse() != null) {
                sum += cs.getCourse().getCredits();
            }
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", qualifiedCourses=" + qualifiedCourses +
                ", teaching=" + teachingAssignment.size() +
                '}';
    }
}

