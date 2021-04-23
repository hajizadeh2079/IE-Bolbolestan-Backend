package IE.server.services;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UnitSelectionSystem {
    private ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Course> courses = new ArrayList<Course>();
    private HashMap<String, Long> codesUnits = new HashMap<>();
    private HashMap<String, String> codesNames = new HashMap<>();
    private static UnitSelectionSystem instance;
    private IOHandler ioHandler;
    private String loggedInStudent;
    private String searchFilter;

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public HashMap<String, Long> getCodesUnits() {
        return codesUnits;
    }

    public HashMap<String, String> getCodesNames() {
        return codesNames;
    }

    public ArrayList<Report> getGradesHistory(String id) throws StudentNotFound {
        ArrayList<Grade> gradesHistory = instance.findStudent(id).getReportCard().getGradesHistory();
        Long maxTerm = 0L;
        for (Grade grade: gradesHistory)
            if (grade.getTerm() > maxTerm)
                maxTerm = grade.getTerm();
        ArrayList<Report> reports = new ArrayList<Report>();
        for (Long i = maxTerm; i > 0; i--) {
            ArrayList<Grade> temp = new ArrayList<Grade>();
            for(Grade grade: gradesHistory)
                if(grade.getTerm().equals(i))
                    temp.add(grade);
            reports.add(new Report(temp, i));
        }
        return reports;
    }

    public void addCourse(String id, String code, String classCode) throws StudentNotFound, OfferingNotFound,
            ExamTimeCollisionError, ClassTimeCollisionError {
        Student student = instance.findStudent(id);
        Course newCourse = instance.findCourse(code, classCode);
        if (newCourse.getRemainingCapacity() > 0)
            instance.addToWeeklySchedule(student, newCourse);
        else
            instance.addToWaitList(student, newCourse);
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public void resetPlan(String id) {
        try {
            findStudent(id).resetPlan();
        } catch (Exception ignore) { }
    }

    public ArrayList<Course> getPlanCourses() {
        try {
            return findStudent(loggedInStudent).getWeeklySchedule().getAllCourses();
        } catch (StudentNotFound studentNotFound) {
            return null;
        }
    }

    public int getTotalSelectedUnits() {
        try {
            return findStudent(loggedInStudent).getWeeklySchedule().sumOfUnits();
        } catch (StudentNotFound studentNotFound) {
            return 0;
        }
    }

    public String getSearchFilter() {
        return searchFilter;
    }

    public ArrayList<Course> getFilteredCourses(String search, String type) {
        if (search.equals(""))
            return courses;
        ArrayList<Course> filteredCourses = new ArrayList<Course>();
        for (Course course: courses)
            if (course.getName().contains(search))
                filteredCourses.add(course);
        return filteredCourses;
    }

    public static UnitSelectionSystem getInstance() {
        if (instance == null) {
            instance = new UnitSelectionSystem();
            instance.ioHandler = new IOHandler();
            instance.prepareData();
            instance.loggedInStudent = null;
            instance.searchFilter = null;
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
        jsonArray = ioHandler.getData("http://138.197.181.131:5100/api/courses");
        instance.addOfferings(jsonArray);
        jsonArray = ioHandler.getData("http://138.197.181.131:5100/api/students");
        instance.addStudents(jsonArray);
        for (Student student: instance.getStudents()) {
            jsonArray = ioHandler.getData("http://138.197.181.131:5100/api/grades/" + student.getId());
            student.setReportCard(new ReportCard(jsonArray, instance.getCodesUnits(), instance.getCodesNames()));
        }
    }

    public void waitListToFinalizedCourse() {
        for (Student student: students)
            student.waitListToFinalizedCourse();
    }

    public void submitPlan(String id) throws StudentNotFound, UnitsMinOrMaxError, CapacityError,
            PrerequisitesError, AlreadyPassedError {
        Student student = findStudent(id);
        WeeklySchedule weeklySchedule = student.getWeeklySchedule();
        checkForUnitsLimitError(weeklySchedule);
        for (Course course: weeklySchedule.getAllCourses()) {
            checkForPrerequisitesError(student, course);
            checkForAlreadyPassedError(student, course);
        }
        for (Course course: weeklySchedule.getFinalizedCourses())
            checkForCapacityError(course);
        for (Course course: weeklySchedule.getNonFinalizedCourses())
            checkForCapacityError(course);
        student.submitPlan();
    }

    public void checkForAlreadyPassedError(Student student, Course course) throws AlreadyPassedError {
        if (student.getReportCard().doesPassCourse(course.getCode()))
            throw new AlreadyPassedError(course.getCode());
    }

    public void checkForCapacityError(Course course) throws CapacityError {
        if (course.getRemainingCapacity() <= 0)
            throw new CapacityError(course.getCode(), course.getClassCode());
    }

    public void checkForUnitsLimitError(WeeklySchedule weeklySchedule) throws UnitsMinOrMaxError {
        int sumOfUnits = weeklySchedule.sumOfUnits();
        if (sumOfUnits > 20)
            throw new UnitsMinOrMaxError("Max");
        if (sumOfUnits < 12)
            throw new UnitsMinOrMaxError("Min");
    }

    public void checkForExamTimeCollisionError(Student student, Course newCourse) throws ExamTimeCollisionError {
        ArrayList<Course> courses = student.getWeeklySchedule().getAllCourses();
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
        ArrayList<Course> courses = student.getWeeklySchedule().getAllCourses();
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

    public void removeFromWeeklySchedule(String id, String code, String classCode) {
        try {
            Student student = findStudent(id);
            Course course = findCourse(code, classCode);
            student.removeFromWeeklySchedule(course);
        } catch (Exception ignore) { }
    }

    public void addToWeeklySchedule(Student student, Course newCourse) throws ClassTimeCollisionError, ExamTimeCollisionError {
        checkForClassTimeCollisionError(student, newCourse);
        checkForExamTimeCollisionError(student, newCourse);
        for (Course course: student.getWeeklySchedule().getAllCourses())
            if (course.getCode().equals(newCourse.getCode()))
                return;
        student.addToWeeklySchedule(newCourse);
    }

    public void addToWaitList(Student student, Course newCourse) throws ClassTimeCollisionError, ExamTimeCollisionError {
        checkForClassTimeCollisionError(student, newCourse);
        checkForExamTimeCollisionError(student, newCourse);
        for (Course course: student.getWeeklySchedule().getAllCourses())
            if (course.getCode().equals(newCourse.getCode()))
                return;
        student.addToWaitList(newCourse);
    }

    public void checkForPrerequisitesError(Student student, Course newCourse) throws PrerequisitesError {
        for(String prerequisite : newCourse.getPrerequisitesArray())
            if(!student.getReportCard().doesPassCourse(prerequisite))
                throw new PrerequisitesError(prerequisite, newCourse.getCode());
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
        codesNames.put(code, name);
    }

    public void addStudents(JSONArray jsonArray) {
        for (Object o : jsonArray) {
            addStudent((JSONObject) o);
        }
    }

    public void addStudent(JSONObject jo) {
        String id = (String)jo.get("id");
        String name = (String)jo.get("name");
        String secondName = (String)jo.get("secondName");
        String birthDate = (String)jo.get("birthDate");
        String field = (String)jo.get("field");
        String faculty = (String)jo.get("faculty");
        String level = (String)jo.get("level");
        String status = (String)jo.get("status");
        String img = (String)jo.get("img");
        Student student = new Student(id, name, secondName, birthDate, field, faculty, level, status, img);
        students.add(student);
    }

    public Student findStudent(String id) throws StudentNotFound {
        for(Student student: students)
            if(student.getId().equals(id))
                return student;
        throw new StudentNotFound();
    }

    public Course findCourse(String code, String classCode) throws OfferingNotFound {
        for(Course course: courses)
            if(course.getCode().equals(code) && course.getClassCode().equals(classCode))
                return course;
        throw new OfferingNotFound();
    }
}