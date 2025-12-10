package com.school.service;

import com.school.model.Classroom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClassroomService {

    private final Map<String, Classroom> rooms = new HashMap<>();

    // Add a classroom to the internal map
    public void addRoom(Classroom r) {
        rooms.put(r.getRoomNumber(), r);
    }

    // Get a classroom by room number
    public Classroom getRoom(String roomNumber) {
        return rooms.get(roomNumber);
    }

    // Get all classrooms
    public Map<String, Classroom> getAll() {
        return rooms;
    }

    // Load classrooms from a CSV file
    public void loadFromCsv(String path) throws IOException {
        Path p = Paths.get(path);

        try (Scanner sc = new Scanner(new File(p.toString()))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                // Skip empty lines
                if (line.trim().isEmpty()) continue;

                // Split CSV line
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) {
                    String room = parts[0].trim();
                    boolean hasComputer = Boolean.parseBoolean(parts[1].trim());
                    boolean hasSmartboard = Boolean.parseBoolean(parts[2].trim());

                    // Create and add classroom
                    addRoom(new Classroom(room, hasComputer, hasSmartboard));
                }
            }
        }
    }
}
