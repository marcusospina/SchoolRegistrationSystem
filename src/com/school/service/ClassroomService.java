package com.school.service;

import com.school.model.Classroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ClassroomService {
    private final Map<String, Classroom> rooms = new HashMap<>();

    public void addRoom(Classroom r) {
        rooms.put(r.getRoomNumber(), r);
    }

    public Classroom getRoom(String roomNumber) {
        return rooms.get(roomNumber);
    }

    public Map<String, Classroom> getAll() { return rooms; }

    public void loadFromCsv(String path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) {
                    String room = parts[0].trim();
                    boolean hasComputer = Boolean.parseBoolean(parts[1].trim());
                    boolean hasSmartboard = Boolean.parseBoolean(parts[2].trim());
                    addRoom(new Classroom(room, hasComputer, hasSmartboard));
                }
            }
        }
    }
}

