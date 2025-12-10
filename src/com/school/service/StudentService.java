package com.school.service;

import com.school.model.Student;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StudentService {

    private final Map<String, Student> students = new HashMap<>();

    public void addStudent(Student s) {
        students.put(s.getId(), s);
    }

    public Student getStudent(String id) {
        return students.get(id);
    }

    public Map<String, Student> getAll() {
        return students;
    }

    public void loadFromCsv(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Student CSV not found: " + filePath);
            return;
        }

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) continue;

                String[] parts = line.split(",");

                if (parts.length < 3) {
                    System.out.println("Invalid student row (skipped): " + line);
                    continue;
                }

                String id = parts[0].trim();
                String name = parts[1].trim();
                String major = parts[2].trim();

                Student student = new Student(id, name, major);
                addStudent(student);
            }

        } catch (Exception e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }
}
