package com.school.service;

import com.school.model.Instructor;
import java.io.File;
import java.util.*;

public class InstructorService {

    private final Map<String, Instructor> instructors = new HashMap<>();

    public void addInstructor(Instructor i) {
        instructors.put(i.getId(), i);
    }

    public Instructor getInstructor(String id) {
        return instructors.get(id);
    }

    public Map<String, Instructor> getAll() {
        return instructors;
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
                    String qualField = parts[2].trim();

                    
                    List<String> qualified;
                    if (qualField.isEmpty()) {
                        qualified = List.of();
                    } else {
                        qualified = Arrays.asList(qualField.split("\\|"));
                    }

                    addInstructor(new Instructor(id, name, qualified));

                } catch (Exception ignore) {
                    
                }
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading instructors: " + e.getMessage());
        }
    }
}
