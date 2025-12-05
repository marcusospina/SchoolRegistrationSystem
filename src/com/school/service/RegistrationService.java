package com.school.service;

import com.school.exception.SchoolException;
import com.school.model.ClassSession;
import com.school.model.Course;
import com.school.model.Instructor;
import com.school.model.Student;
import com.school.model.Classroom;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RegistrationService {
    private final InstructorService instructorService;
    private final CourseService courseService;
    private final ClassroomService classroomService;
    private final StudentService studentService;

    // Global registry of sessions: key = COURSEID-sectionNumber
    private final Map<String, ClassSession> sessions = new LinkedHashMap<>();
    private final Map<String, AtomicInteger> nextSectionNumber = new HashMap<>();

    public RegistrationService(InstructorService instructorService,
                               CourseService courseService,
                               ClassroomService classroomService,
                               StudentService studentService) {
        this.instructorService = instructorService;
        this.courseService = courseService;
        this.classroomService = classroomService;
        this.studentService = studentService;
    }

    public List<Instructor> findEligibleInstructors(Course c) {
        List<Instructor> result = new ArrayList<>();
        for (Instructor instr : instructorService.getAll().values()) {
            if (instr.canTeach(c)) result.add(instr);
        }
        return result;
    }

    public ClassSession createClassSection(Course c, Instructor i, Classroom r, int capacity)
            throws SchoolException {
        if (c == null) throw new SchoolException("Course cannot be null");
        if (i == null) throw new SchoolException("Instructor cannot be null");
        if (r == null) throw new SchoolException("Classroom cannot be null");

        if (!i.canTeach(c)) {
            throw new SchoolException("Instructor not qualified to teach " + c.getCourseId());
        }

        int projectedLoad = i.getCurrentLoad() + c.getCredits();
        if (projectedLoad > 9) {
            throw new SchoolException("Instructor load exceeded (would be " + projectedLoad + " credits)");
        }

        AtomicInteger counter = nextSectionNumber.computeIfAbsent(c.getCourseId(), k -> new AtomicInteger(1));
        int sectionNumber = counter.getAndIncrement();

        ClassSession session = new ClassSession(c, i, r, sectionNumber, capacity);

        // link to instructor and store globally
        i.getTeachingAssignment().add(session);
        String key = makeSessionKey(c.getCourseId(), sectionNumber);
        sessions.put(key, session);

        return session;
    }

    private String makeSessionKey(String courseId, int sectionNumber) {
        return courseId + "-" + sectionNumber;
    }

    public void registerStudent(Student s, ClassSession section) throws SchoolException {
        if (s == null) throw new SchoolException("Student cannot be null");
        if (section == null) throw new SchoolException("Section cannot be null");

        if (section.isFull()) {
            throw new SchoolException("Section is full");
        }

        int projected = s.getCurrentCredits() + section.getCourse().getCredits();
        if (projected > 18) {
            throw new SchoolException("Student credit limit exceeded (would be " + projected + " credits)");
        }

        if (section.getEnrolledStudents().stream().anyMatch(st -> st.getId().equals(s.getId()))) {
            throw new SchoolException("Student already enrolled in section");
        }

        section.getEnrolledStudents().add(s);
        s.getEnrolledClasses().add(section);
    }

    public Map<String, ClassSession> getAllSessions() {
        return sessions;
    }

    public ClassSession getSession(String key) {
        return sessions.get(key);
    }

    // convenience to get Course / Student / Instructor / Classroom by ID
    public Course getCourse(String id) { return courseService.getCourse(id); }
    public Student getStudent(String id) { return studentService.getStudent(id); }
    public Instructor getInstructor(String id) { return instructorService.getInstructor(id); }
    public Classroom getClassroom(String id) { return classroomService.getRoom(id); }
}

