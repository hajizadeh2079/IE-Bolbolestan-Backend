package com.example.model;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UnitSelectionSystem {
    private ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Course> courses = new ArrayList<Course>();
    private HashMap<String, Long> codesUnits = new HashMap<>();
    private static UnitSelectionSystem instance;
    private IOHandler ioHandler;
    private String loggedInStudent;

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public HashMap<String, Long> getCodesUnits() {
        return codesUnits;
    }

    public static UnitSelectionSystem getInstance() {
        if (instance == null) {
            instance = new UnitSelectionSystem();
            instance.ioHandler = new IOHandler();
            instance.prepareData();
            instance.loggedInStudent = null;
        }
        return instance;
    }

    public String getLoggedInStudent() {
        return loggedInStudent;
    }

    public void setLoggedInStudent(String id) {
        loggedInStudent = id;
    }

    public void logoutStudent() {
        loggedInStudent = null;
    }

    public void prepareData() {
        JSONArray jsonArray;
        jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/courses");
        instance.addOfferings(jsonArray);
        jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/students");
        instance.addStudents(jsonArray);
        for (Student student: instance.getStudents()) {
            jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/grades/" + student.getId());
            student.setReportCard(new ReportCard(jsonArray, instance.getCodesUnits()));
        }
    }

    public boolean finalize(String studentId) throws StudentNotFound {
        Student student = findStudent(studentId);
        int sumOfUnits = student.getWeeklySchedule().sumOfUnits();
        if (sumOfUnits <= 20 && sumOfUnits >= 12) {
            student.getWeeklySchedule().finalizeSchedule();
            return true;
        }
        return false;
    }

    public void checkForExamTimeCollisionError(Student student, Course newCourse) throws ExamTimeCollisionError {
        ArrayList<Course> courses = student.getWeeklySchedule().getCourses();
        for (Course course : courses) {
            if (course.getCode().equals(newCourse.getCode()))
                continue;
            if (checkForCollisionExamTime(course, newCourse))
                throw new ExamTimeCollisionError(course.getCode(), course.getClassCode(), newCourse.getCode(), newCourse.getClassCode());
        }
    }

    public boolean checkForCollisionExamTime(Course course1, Course course2) {
        long start1ToSeconds = course1.getExamTimeStart().toEpochSecond(ZoneOffset.UTC);
        long end1ToSeconds = course1.getExamTimeEnd().toEpochSecond(ZoneOffset.UTC);
        long start2ToSeconds = course2.getExamTimeStart().toEpochSecond(ZoneOffset.UTC);
        long end2ToSeconds = course2.getExamTimeEnd().toEpochSecond(ZoneOffset.UTC);
        return start1ToSeconds < end2ToSeconds && start2ToSeconds < end1ToSeconds;
    }

    public void checkForClassTimeCollisionError(Student student, Course newCourse) throws ClassTimeCollisionError {
        ArrayList<Course> courses = student.getWeeklySchedule().getCourses();
        for (Course course : courses) {
            if (course.getCode().equals(newCourse.getCode()))
                continue;
            if (checkForCollisionClassTime(course, newCourse))
                throw new ClassTimeCollisionError(course.getCode(), course.getClassCode(), newCourse.getCode(), newCourse.getClassCode());
        }
    }

    public boolean checkForCollisionClassTime(Course course1, Course course2) {
        boolean haveSameDay = false;
        for (String classDay1 : course1.getClassTimeDays()) {
            for (String classDay2: course2.getClassTimeDays()) {
                if (classDay1.equals(classDay2)) {
                    haveSameDay = true;
                    break;
                }
            }
        }
        if (haveSameDay) {
            int start1ToSeconds = course1.getClassTimeStart().toSecondOfDay();
            int end1ToSeconds = course1.getClassTimeEnd().toSecondOfDay();
            int start2ToSeconds = course2.getClassTimeStart().toSecondOfDay();
            int end2ToSeconds = course2.getClassTimeEnd().toSecondOfDay();
            return start1ToSeconds < end2ToSeconds && start2ToSeconds < end1ToSeconds;
        }
        return false;
    }

    public void removeFromWeeklySchedule(String studentId, String code, String classCode) throws Exception {
        Student student = findStudent(studentId);
        Course course = findCourse(code, classCode);
        student.removeFromWeeklySchedule(course);
    }

    public void addToWeeklySchedule(String studentId, String code, String classCode) throws Exception {
        Student student = findStudent(studentId);
        Course newCourse = findCourse(code, classCode);
        checkForPrerequisitesError(student, newCourse);
        checkForClassTimeCollisionError(student, newCourse);
        checkForExamTimeCollisionError(student, newCourse);
        for (Course course: student.getWeeklySchedule().getCourses())
            if (course.getCode().equals(newCourse.getCode()))
                return;
        student.addToWeeklySchedule(newCourse);
    }

    public void checkForPrerequisitesError(Student student, Course newCourse) throws PrerequisitesError{
        for(String prerequisite : newCourse.getPrerequisitesArray())
            if(!student.getReportCard().doesPassCourse(prerequisite))
                throw new PrerequisitesError(prerequisite);
    }

    public void addOfferings(JSONArray jsonArray) {
        for (Object o : jsonArray) {
            addOffering((JSONObject) o);
        }
    }

    public void addOffering(JSONObject jo) {
        String code = (String)jo.get("code");
        String name = (String)jo.get("name");
        String instructor = (String)jo.get("instructor");
        long units = (Long)jo.get("units");
        JSONObject classTime = (JSONObject)jo.get("classTime");
        JSONObject examTime = (JSONObject)jo.get("examTime");
        long capacity = (Long) jo.get("capacity");
        JSONArray prerequisites = (JSONArray)jo.get("prerequisites");
        String classCode = (String)jo.get("classCode");
        String type = (String)jo.get("type");
        Course course = new Course(code, classCode, name, instructor, type, units, classTime, examTime, capacity, prerequisites);
        int index = 0;
        for (index = 0; index < courses.size(); index++)
            if (name.equals(courses.get(index).getName()))
                break;
        courses.add(index, course);
        codesUnits.put(code, units);
    }

    public void addStudents(JSONArray jsonArray) {
        for (Object o : jsonArray) {
            addStudent((JSONObject) o);
        }
    }

    public void addStudent(JSONObject jo) {
        String id = (String)jo.get("id");
        String name = (String)jo.get("name");
        String enteredAt = (String)jo.get("secondName");
        String birthDate = (String)jo.get("birthDate");
        Student student = new Student(id, name, enteredAt, birthDate);
        students.add(student);
    }

    public Student findStudent(String id)  throws StudentNotFound {
        for(Student student: students)
            if(student.getId().equals(id))
                return student;
        throw new StudentNotFound();
    }

    public Course findCourse(String code, String classCode)  throws OfferingNotFound {
        for(Course course: courses)
            if(course.getCode().equals(code) && course.getClassCode().equals(classCode))
                return course;
        throw new OfferingNotFound();
    }
}