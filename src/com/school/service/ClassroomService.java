package com.school.app.service;

import com.school.model.Classroom;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClassroomService {

    public static Map<String, Classroom> load() {
        Map<String, Classroom> classrooms = new HashMap<>();

        try {
            Scanner sc = new Scanner(new File("data/Classroom.csv"));

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String room = parts[0].trim();
                boolean hasComp = Boolean.parseBoolean(parts[1].trim());
                boolean hasBoard = Boolean.parseBoolean(parts[2].trim());

                classrooms.put(room, new Classroom(room, hasComp, hasBoard));
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading Classroom.csv: " + e.getMessage());
        }

        return classrooms;
    }
}
