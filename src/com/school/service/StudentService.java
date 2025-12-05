package com.school.service;

import com.school.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StudentService {
    private final Map<String, Student> students = new HashMap<>();

    public void addStudent(Student s) {
        students.put(s.getId(), s);
    }

    public Student getStudent(String id) { return students.get(id); }

    public Map<String, Student> getAll() { return students; }

    public void loadFromCsv(String path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) {
                    addStudent(new Student(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }
        }
    }
}

