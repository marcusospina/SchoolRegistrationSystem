package com.school.app;

import com.school.model.*;
import com.school.service.*;
import com.school.view.SchoolSystemUI;

public class Main {
    public static void main(String[] args) {
        // instantiate services
        CourseService courseService = new CourseService();
        StudentService studentService = new StudentService();
        InstructorService instructorService = new InstructorService();
        ClassroomService classroomService = new ClassroomService();

        RegistrationService registrationService = new RegistrationService(
                instructorService, courseService, classroomService, studentService
        );

        // UI
        SchoolSystemUI ui = new SchoolSystemUI(
                studentService,
                instructorService,
                courseService,
                classroomService,
                registrationService
        );

        // run menu loop
        ui.runMainLoop();
    }
}

