package com.school.view;

import com.school.exception.SchoolException;
import com.school.model.ClassSession;
import com.school.model.Course;
import com.school.model.Instructor;
import com.school.model.Student;
import com.school.model.Classroom;
import com.school.service.*;

import java.io.IOException;
import java.util.List;
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
            System.out.println("1. Load Data (CSVs)");
            System.out.println("2. View Students / Instructors / Courses / Classrooms");
            System.out.println("3. Create Class Section");
            System.out.println("4. Register Student");
            System.out.println("5. View Section Reports");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    loadData();
                    break;
                case "2":
                    viewEntities();
                    break;
                case "3":
                    createSectionFlow();
                    break;
                case "4":
                    registerStudentFlow();
                    break;
                case "5":
                    viewSectionReports();
                    break;
                case "6":
                    System.out.println("Exiting. Goodbye.");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }

    private void loadData() {
        try {
            courseService.loadFromCsv("data/courses.csv");
            studentService.loadFromCsv("data/students.csv");
            instructorService.loadFromCsv("data/instructors.csv");
            classroomService.loadFromCsv("data/classrooms.csv");
            System.out.println("Data loaded successfully from data/*.csv");
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private void viewEntities() {
        System.out.println("\nView:");
        System.out.println("1. Students");
        System.out.println("2. Instructors");
        System.out.println("3. Courses");
        System.out.println("4. Classrooms");
        System.out.print("Choose: ");

        String c = scanner.nextLine().trim();

        switch (c) {
            case "1":
                System.out.printf("\n%-8s %-20s %-20s %-8s%n", "ID", "Name", "Major", "Credits");
                for (Student s : studentService.getAll().values()) {
                    System.out.printf("%-8s %-20s %-20s %-8d%n",
                            s.getId(), s.getName(), s.getMajor(), s.getCurrentCredits());
                }
                break;

            case "2":
                System.out.printf("\n%-8s %-25s %-20s %-8s%n", "ID", "Name", "QualifiedCourses", "Load");
                for (Instructor i : instructorService.getAll().values()) {
                    System.out.printf("%-8s %-25s %-20s %-8d%n",
                            i.getId(), i.getName(),
                            String.join("|", i.getQualifiedCourses()),
                            i.getCurrentLoad());
                }
                break;

            case "3":
                System.out.printf("\n%-8s %-30s %-8s%n", "CourseID", "Name", "Credits");
                for (Course cObj : courseService.getAll().values()) {
                    System.out.printf("%-8s %-30s %-8d%n",
                            cObj.getCourseId(), cObj.getName(), cObj.getCredits());
                }
                break;

            case "4":
                System.out.printf("\n%-10s %-10s %-12s%n", "Room", "Computer", "Smartboard");
                for (Classroom r : classroomService.getAll().values()) {
                    System.out.printf("%-10s %-10s %-12s%n",
                            r.getRoomNumber(), r.isHasComputer(), r.isHasSmartboard());
                }
                break;

            default:
                System.out.println("Invalid selection.");
                break;
        }
    }

    private void createSectionFlow() {
        System.out.print("Enter Course ID (e.g., CS101): ");
        String courseId = scanner.nextLine().trim();
        Course c = registrationService.getCourse(courseId);
        if (c == null) {
            System.out.println("Course not found.");
            return;
        }

        List<Instructor> eligible = registrationService.findEligibleInstructors(c);
        if (eligible.isEmpty()) {
            System.out.println("No eligible instructors found for this course.");
            return;
        }

        System.out.printf("\nEligible Instructors for %s:%n", courseId);
        System.out.printf("%-8s %-25s %-8s%n", "ID", "Name", "CurrentLoad");
        for (Instructor i : eligible) {
            System.out.printf("%-8s %-25s %-8d%n",
                    i.getId(), i.getName(), i.getCurrentLoad());
        }

        System.out.print("Enter Instructor ID: ");
        String instrId = scanner.nextLine().trim();
        Instructor instr = registrationService.getInstructor(instrId);
        if (instr == null) {
            System.out.println("Instructor not found.");
            return;
        }

        System.out.print("Enter Classroom Room Number (e.g., RM101): ");
        String room = scanner.nextLine().trim();
        Classroom r = registrationService.getClassroom(room);
        if (r == null) {
            System.out.println("Classroom not found.");
            return;
        }

        System.out.print("Enter Section Capacity (int): ");
        int capacity;
        try {
            capacity = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid capacity.");
            return;
        }

        try {
            ClassSession session = registrationService.createClassSection(c, instr, r, capacity);
            System.out.println("Section created: "
                    + c.getCourseId() + "-" + session.getSectionNumber());
        } catch (SchoolException e) {
            System.err.println("Could not create section: " + e.getMessage());
        }
    }

    private void registerStudentFlow() {
        System.out.print("Enter Student ID: ");
        String sid = scanner.nextLine().trim();
        Student s = registrationService.getStudent(sid);
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        Map<String, ClassSession> all = registrationService.getAllSessions();
        if (all.isEmpty()) {
            System.out.println("No sections available. Create a section first.");
            return;
        }

        System.out.printf("\nAvailable Sections:%n");
        System.out.printf("%-10s %-10s %-25s %-10s %-10s%n",
                "SectionKey", "Course", "Instructor", "Enrolled", "Capacity");

        for (Map.Entry<String, ClassSession> e : all.entrySet()) {
            ClassSession cs = e.getValue();
            System.out.printf("%-10s %-10s %-25s %-10d %-10d%n",
                    e.getKey(),
                    cs.getCourse().getCourseId(),
                    cs.getInstructor().getName(),
                    cs.getEnrolledStudents().size(),
                    cs.getMaxCapacity());
        }

        System.out.print("Enter Section Key to register (e.g., CS101-1): ");
        String key = scanner.nextLine().trim();
        ClassSession session = registrationService.getSession(key);
        if (session == null) {
            System.out.println("Section not found.");
            return;
        }

        try {
            registrationService.registerStudent(s, session);
            System.out.println("Student registered successfully.");
        } catch (SchoolException ex) {
            System.err.println("Registration failed: " + ex.getMessage());
        }
    }

    private void viewSectionReports() {
        Map<String, ClassSession> all = registrationService.getAllSessions();
        if (all.isEmpty()) {
            System.out.println("No sections created.");
            return;
        }

        System.out.printf("\n%-10s %-10s %-25s %-10s %-10s %-20s%n",
                "Section", "Course", "Instructor", "Enrolled", "Capacity", "Classroom");

        for (Map.Entry<String, ClassSession> e : all.entrySet()) {
            ClassSession cs = e.getValue();

            System.out.printf("%-10s %-10s %-25s %-10d %-10d %-20s%n",
                    e.getKey(),
                    cs.getCourse().getCourseId(),
                    cs.getInstructor().getName(),
                    cs.getEnrolledStudents().size(),
                    cs.getMaxCapacity(),
                    cs.getClassroom().getRoomNumber());

            if (!cs.getEnrolledStudents().isEmpty()) {
                System.out.println("  Enrolled Students:");
                System.out.printf("    %-8s %-20s %-8s%n", "ID", "Name", "Credits");
                for (Student st : cs.getEnrolledStudents()) {
                    System.out.printf("    %-8s %-20s %-8d%n",
                            st.getId(), st.getName(), st.getCurrentCredits());
                }
            }

            System.out.println();
        }
    }
}
