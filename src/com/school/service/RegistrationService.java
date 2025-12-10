package com.school.service;

import com.school.exception.SchoolException;
import com.school.model.*;
import java.util.*;

public class RegistrationService {

    private final InstructorService instructorService;
    private final CourseService courseService;
    private final ClassroomService classroomService;
    private final StudentService studentService;

    
    private final Map<String, ClassSession> sessions = new LinkedHashMap<>();

    
    private final Map<String, Integer> nextSectionNumber = new HashMap<>();

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
        List<Instructor> eligible = new ArrayList<>();
        for (Instructor instr : instructorService.getAll().values()) {
            if (instr.canTeach(c)) {
                eligible.add(instr);
            }
        }
        return eligible;
    }

    
    public ClassSession createClassSection(Course c, Instructor i, Classroom r, int capacity)
            throws SchoolException {

        require(c, "Course cannot be null");
        require(i, "Instructor cannot be null");
        require(r, "Classroom cannot be null");

        if (!i.canTeach(c)) {
            throw new SchoolException("Instructor not qualified to teach " + c.getCourseId());
        }

        int newLoad = i.getCurrentLoad() + c.getCredits();
        if (newLoad > 9) {
            throw new SchoolException("Instructor load exceeded (would be " + newLoad + ")");
        }

       
        int sectionNumber = getNextSection(c.getCourseId());

        ClassSession session = new ClassSession(c, i, r, sectionNumber, capacity);

       
        i.getTeachingAssignment().add(session);
        sessions.put(makeKey(c.getCourseId(), sectionNumber), session);

        return session;
    }

    private int getNextSection(String courseId) {
        int next = nextSectionNumber.getOrDefault(courseId, 1);
        nextSectionNumber.put(courseId, next + 1); 
        return next;
    }

    private String makeKey(String courseId, int sectionNum) {
        return courseId + "-" + sectionNum;
    }

    
    public void registerStudent(Student s, ClassSession section) throws SchoolException {

        require(s, "Student cannot be null");
        require(section, "Section cannot be null");

        if (section.isFull()) {
            throw new SchoolException("Section is full");
        }

        int newCreditTotal = s.getCurrentCredits() + section.getCourse().getCredits();
        if (newCreditTotal > 18) {
            throw new SchoolException("Student credit limit exceeded (would be " + newCreditTotal + ")");
        }

        boolean alreadyEnrolled =
                section.getEnrolledStudents().stream().anyMatch(st -> st.getId().equals(s.getId()));

        if (alreadyEnrolled) {
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

    public Course getCourse(String id) {
        return courseService.getCourse(id);
    }

    public Student getStudent(String id) {
        return studentService.getStudent(id);
    }

    public Instructor getInstructor(String id) {
        return instructorService.getInstructor(id);
    }

    public Classroom getClassroom(String id) {
        return classroomService.getRoom(id);
    }

    
    private void require(Object obj, String message) throws SchoolException {
        if (obj == null) {
            throw new SchoolException(message);
        }
    }
}
