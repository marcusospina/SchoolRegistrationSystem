package com.school.app;

import com.school.service.*;
import com.school.view.SchoolSystemUI;

public class Main {
    public static void main(String[] args) {

        // Create services
        CourseService courseService = new CourseService();
        StudentService studentService = new StudentService();
        InstructorService instructorService = new InstructorService();
        ClassroomService classroomService = new ClassroomService();

        // Registration service depends on the others
        RegistrationService registrationService = new RegistrationService(
                instructorService,
                courseService,
                classroomService,
                studentService
        );

        // Create the UI and run the system
        SchoolSystemUI ui = new SchoolSystemUI(
                studentService,
                instructorService,
                courseService,
                classroomService,
                registrationService
        );

        ui.runMainLoop();
    }
}
