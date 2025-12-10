package com.school.service;

import com.school.model.Course;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CourseService {

    private final Map<String, Course> courses = new HashMap<>();

    public void addCourse(Course c) {
        courses.put(c.getCourseId(), c);
    }

    public Course getCourse(String id) {
        return courses.get(id);
    }

    public Map<String, Course> getAll() {
        return courses;
    }

    public void loadFromCsv(String path) {
        try {
            Scanner sc = new Scanner(new File(path));

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");

            
                if (parts.length < 3) continue;

                try {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int credits = Integer.parseInt(parts[2].trim());

                    addCourse(new Course(id, name, credits));
                } catch (Exception ignore) {
 
                }
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Unable to load courses: " + e.getMessage());
        }
    }
}
