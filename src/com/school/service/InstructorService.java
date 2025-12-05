package com.school.service;

import com.school.model.Instructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructorService {
    private final Map<String, Instructor> instructors = new HashMap<>();

    public void addInstructor(Instructor i) {
        instructors.put(i.getId(), i);
    }

    public Instructor getInstructor(String id) { return instructors.get(id); }

    public Map<String, Instructor> getAll() { return instructors; }

    public void loadFromCsv(String path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String qual = parts[2].trim();
                    List<String> qualified = qual.isEmpty() ? List.of() : Arrays.asList(qual.split("\\|"));
                    addInstructor(new Instructor(id, name, qualified));
                }
            }
        }
    }
}

