package com.school.view;

import com.school.exception.SchoolException;
import com.school.model.*;
import com.school.service.*;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class SchoolSystemUI {

    private final StudentService studentService;
    private final InstructorService instructorService;
    private final CourseService courseService;
    private final ClassroomService classroomService;
    private final RegistrationService registrationService;

    private final Scanner scanner = new Scanner(System.in);

    public SchoolSystemUI(StudentService studentService,
                          InstructorService instructorService,
                          CourseService courseService,
                          ClassroomService classroomService,
                          RegistrationService registrationService) {
        this.studentService = studentService;
        this.instructorService = instructorService;
        this.courseService = courseService;
        this.classroomService = classroomService;
        this.registrationService = registrationService;
    }

    public void runMainLoop() {
        while (true) {
            System.out.println("\n--- School System Menu ---");
            System.out.println("1. Load Data");
            System.out.println("2. View Data");
            System.out.println("3. Create Section");
            System.out.println("4. Register Student");
            System.out.println("5. Section Reports");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            if (choice.equals("6")) {
                System.out.println("Goodbye.");
                return;
            }

            switch (choice) {
                case "1": loadData(); break;
                case "2": viewData(); break;
                case "3": createSection(); break;
                case "4": registerStudent(); break;
                case "5": sectionReports(); break;
                default:  System.out.println("Invalid choice."); break;
            }
        }
    }

    // ---------------------------------------------------------
    // Load CSVs
    // ---------------------------------------------------------
    private void loadData() {
        try {
            studentService.loadFromCsv("data/students.csv");
            instructorService.loadFromCsv("data/instructors.csv");
            courseService.loadFromCsv("data/courses.csv");
            classroomService.loadFromCsv("data/classrooms.csv");
            System.out.println("Data loaded.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // View Students / Instructors / Courses / Classrooms
    // ---------------------------------------------------------
    private void viewData() {
        System.out.println("\nView:");
        System.out.println("1. Students");
        System.out.println("2. Instructors");
        System.out.println("3. Courses");
        System.out.println("4. Classrooms");
        System.out.print("Choose: ");

        String c = scanner.nextLine();

        System.out.println();

        if (c.equals("1")) {
            System.out.printf("%-10s %-20s %-20s%n", "ID", "Name", "Major");
            for (Student s : studentService.getAll().values()) {
                System.out.printf("%-10s %-20s %-20s%n",
                        s.getId(), s.getName(), s.getMajor());
            }
        }
        else if (c.equals("2")) {
            System.out.printf("%-10s %-25s %-20s%n", "ID", "Name", "Qualified");
            for (Instructor i : instructorService.getAll().values()) {
                String q = String.join("|", i.getQualifiedCourses());
                System.out.printf("%-10s %-25s %-20s%n",
                        i.getId(), i.getName(), q);
            }
        }
        else if (c.equals("3")) {
            System.out.printf("%-10s %-30s %-10s%n", "CourseID", "Name", "Credits");
            for (Course co : courseService.getAll().values()) {
                System.out.printf("%-10s %-30s %-10d%n",
                        co.getCourseId(), co.getName(), co.getCredits());
            }
        }
        else if (c.equals("4")) {
            System.out.printf("%-12s %-12s %-12s%n", "Room", "Computer", "Smartboard");
            for (Classroom r : classroomService.getAll().values()) {
                System.out.printf("%-12s %-12s %-12s%n",
                        r.getRoomNumber(), r.isHasComputer(), r.isHasSmartboard());
            }
        }
        else {
            System.out.println("Invalid option.");
        }
    }

    // ---------------------------------------------------------
    // Create Section
    // ---------------------------------------------------------
    private void createSection() {
        System.out.print("Course ID: ");
        String courseId = scanner.nextLine();

        Course course = registrationService.getCourse(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        Instructor chosenInstructor = chooseInstructor(course);
        if (chosenInstructor == null) return;

        Classroom chosenRoom = chooseClassroom();
        if (chosenRoom == null) return;

        System.out.print("Capacity: ");
        int cap;
        try {
            cap = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number.");
            return;
        }

        try {
            ClassSession cs = registrationService.createClassSection(course, chosenInstructor, chosenRoom, cap);
            System.out.println("Section created: " + course.getCourseId() + "-" + cs.getSectionNumber());
        } catch (SchoolException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Instructor chooseInstructor(Course c) {
        Instructor[] list = registrationService.findEligibleInstructors(c).toArray(new Instructor[0]);

        System.out.println("\nEligible Instructors:");
        System.out.printf("%-10s %-25s %-10s%n", "ID", "Name", "Load");

        for (int i = 0; i < list.length; i++) {
            Instructor ins = list[i];
            System.out.printf("%-10s %-25s %-10d%n",
                    ins.getId(), ins.getName(), ins.getCurrentLoad());
        }

        if (list.length == 0) {
            System.out.println("None available.");
            return null;
        }

        System.out.print("Instructor ID: ");
        return registrationService.getInstructor(scanner.nextLine());
    }

    private Classroom chooseClassroom() {
        System.out.println("\nAvailable Classrooms:");
        System.out.printf("%-12s %-12s %-12s%n", "Room", "Computer", "Smart");

        for (Classroom r : classroomService.getAll().values()) {
            System.out.printf("%-12s %-12s %-12s%n",
                    r.getRoomNumber(), r.isHasComputer(), r.isHasSmartboard());
        }

        System.out.print("Room: ");
        return registrationService.getClassroom(scanner.nextLine());
    }

    // ---------------------------------------------------------
    // Register Student
    // ---------------------------------------------------------
    private void registerStudent() {
        System.out.print("Student ID: ");
        Student s = registrationService.getStudent(scanner.nextLine());

        if (s == null) {
            System.out.println("Not found.");
            return;
        }

        Map<String, ClassSession> sessions = registrationService.getAllSessions();
        if (sessions.isEmpty()) {
            System.out.println("No sections.");
            return;
        }

        System.out.printf("\n%-12s %-12s %-25s %-10s %-10s%n",
                "Section", "Course", "Instructor", "Enrolled", "Cap");

        for (String key : sessions.keySet()) {
            ClassSession cs = sessions.get(key);
            System.out.printf("%-12s %-12s %-25s %-10d %-10d%n",
                    key,
                    cs.getCourse().getCourseId(),
                    cs.getInstructor().getName(),
                    cs.getEnrolledStudents().size(),
                    cs.getMaxCapacity());
        }

        System.out.print("Section Key: ");
        ClassSession chosen = registrationService.getSession(scanner.nextLine());

        if (chosen == null) {
            System.out.println("Not found.");
            return;
        }

        try {
            registrationService.registerStudent(s, chosen);
            System.out.println("Registered.");
        } catch (SchoolException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // Section Reports
    // ---------------------------------------------------------
    private void sectionReports() {
        Map<String, ClassSession> sessions = registrationService.getAllSessions();

        if (sessions.isEmpty()) {
            System.out.println("No sections.");
            return;
        }

        System.out.printf("\n%-12s %-12s %-25s %-10s %-10s %-15s%n",
                "Section", "Course", "Instructor", "Enrolled", "Cap", "Room");

        for (String key : sessions.keySet()) {
            ClassSession cs = sessions.get(key);
            System.out.printf("%-12s %-12s %-25s %-10d %-10d %-15s%n",
                    key,
                    cs.getCourse().getCourseId(),
                    cs.getInstructor().getName(),
                    cs.getEnrolledStudents().size(),
                    cs.getMaxCapacity(),
                    cs.getClassroom().getRoomNumber());
        }
    }
}
