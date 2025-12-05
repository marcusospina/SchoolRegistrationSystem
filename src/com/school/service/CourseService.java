package com.school.service;

import com.school.model.Course;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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

    public void loadFromCsv(String path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int credits = Integer.parseInt(parts[2].trim());
                    addCourse(new Course(id, name, credits));
                }
            }
        }
    }
}

