package IE.server.services;

import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;

import IE.server.controllers.models.CourseDTO;
import IE.server.controllers.models.GradeDTO;
import IE.server.controllers.models.ReportDTO;
import IE.server.exceptions.StudentNotFound;
import IE.server.repository.*;
import IE.server.repository.models.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UnitSelectionSystem {
    private ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Course> courses = new ArrayList<Course>();
    private HashMap<String, Long> codesUnits = new HashMap<>();
    private HashMap<String, String> codesNames = new HashMap<>();
    private static UnitSelectionSystem instance;
    private IOHandler ioHandler;

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

    public ArrayList<ReportDTO> getGradesHistory(String id) throws SQLException {
        int maxTerm = GradeRepository.getInstance().getMaxTermById(id);
        ArrayList<ReportDTO> reportsDTO = new ArrayList<ReportDTO>();
        for (int term = maxTerm; term > 0; term--) {
            ArrayList<GradeDTO> gradesHistory = GradeRepository.getInstance().getReportCard(id, term);
            double gpa = instance.calcGPA(GradeRepository.getInstance().getTermGrades(id, term));
            reportsDTO.add(new ReportDTO(gradesHistory, term, gpa));
        }
        return reportsDTO;
    }
/*
    public void addCourse(String id, String code, String classCode) throws StudentNotFound, OfferingNotFound,
            ExamTimeCollisionError, ClassTimeCollisionError {
        Student student = instance.findStudent(id);
        Course newCourse = instance.findCourse(code, classCode);
        if (newCourse.getRemainingCapacity() > 0)
            instance.addToWeeklySchedule(student, newCourse);
        else
            instance.addToWaitList(student, newCourse);
    }

    public void resetPlan(String id) {
        try {
            findStudent(id).resetPlan();
        } catch (Exception ignore) { }
    }
*/
    public ArrayList<CourseDTO> getFilteredCourses(String search, String type) {
        try {
            ArrayList<CourseDAO> courseDAOS = CourseRepository.getInstance().getFilteredCourses(search, type);
            ArrayList<CourseDTO> filteredCourses = new ArrayList<CourseDTO>();
            for (CourseDAO courseDAO: courseDAOS) {
                ArrayList<String> prerequisitesNamesArray = PrerequisiteRepository.getInstance().getPrerequisitesNames(courseDAO.getCode(), courseDAO.getClassCode());
                ArrayList<String> classTimeDays = ClassDayRepository.getInstance().getClassDays(courseDAO.getCode(), courseDAO.getClassCode());
                // Todo
                int signedUp = 0;
                // Todo
                filteredCourses.add(new CourseDTO(courseDAO.getCode(), courseDAO.getClassCode(), courseDAO.getName(), courseDAO.getInstructor(), courseDAO.getUnits(), courseDAO.getType(), classTimeDays, courseDAO.getClassTimeStart(), courseDAO.getClassTimeEnd(), courseDAO.getExamTimeStart(), courseDAO.getExamTimeEnd(), courseDAO.getCapacity(), prerequisitesNamesArray, signedUp));
            }
            return filteredCourses;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return new ArrayList<CourseDTO>();
        }
    }

    public static UnitSelectionSystem getInstance() {
        if (instance == null) {
            instance = new UnitSelectionSystem();
            instance.ioHandler = new IOHandler();
            instance.prepareData();
        }
        return instance;
    }

    public void prepareData() {
        JSONArray jsonArray;
        jsonArray = ioHandler.getData("http://138.197.181.131:5100/api/courses");
        instance.addOfferings(jsonArray);
        instance.setPrerequisites(jsonArray);
        jsonArray = ioHandler.getData("http://138.197.181.131:5100/api/students");
        instance.addStudents(jsonArray);
        for (String id: instance.getStudentsId()) {
            jsonArray = ioHandler.getData("http://138.197.181.131:5100/api/grades/" + id);
            instance.setGrades(id, jsonArray);
        }
    }

    public int calcTPU (String id) {
        try {
            return GradeRepository.getInstance().calcTPU(id);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return 0;
        }
    }

    public double calcFinalGPA (String id) {
        try {
            return instance.calcGPA(GradeRepository.getInstance().getLastGrades(id));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return -1;
        }
    }

    public double calcGPA(ArrayList<GradeUnitDAO> grades) {
        int sumOfUnits = 0;
        double sum = 0.0;
        for (GradeUnitDAO gradeUnitDAO: grades) {
            sumOfUnits += gradeUnitDAO.getUnits();
            sum += (gradeUnitDAO.getGrade() * gradeUnitDAO.getUnits());
        }
        return sum / sumOfUnits;
    }

    public void waitListToFinalizedCourse() {
        for (Student student: students)
            student.waitListToFinalizedCourse();
    }
/*
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
*/
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
/*
    public void removeFromWeeklySchedule(String id, String code, String classCode) {
        try {
            Student student = findStudent(id);
            Course course = findCourse(code, classCode);
            student.removeFromWeeklySchedule(course);
        } catch (Exception ignore) { }
    }
*/
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
/*
    public void checkForPrerequisitesError(Student student, Course newCourse) throws PrerequisitesError {
        for(String prerequisite : newCourse.getPrerequisitesArray())
            if(!student.getReportCard().doesPassCourse(prerequisite))
                throw new PrerequisitesError(prerequisite, newCourse.getCode());
    }
*/
    public void addOfferings(JSONArray jsonArray) {
        for (Object o : jsonArray) {
            addOffering((JSONObject) o);
        }
    }

    public void setPrerequisites(JSONArray jsonArray) {
        for (Object o : jsonArray) {
            setPrerequisite((JSONObject) o);
        }
    }

    public void setGrades(String id, JSONArray jsonArray) {
        try {
            for (Object o : jsonArray) {
                JSONObject temp = (JSONObject) o;
                String code = (String) temp.get("code");
                int grade = ((Long) temp.get("grade")).intValue();
                int term = ((Long) temp.get("term")).intValue();
                GradeDAO gradeDAO = new GradeDAO(id, code, term, grade);
                GradeRepository.getInstance().insert(gradeDAO);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void setPrerequisite(JSONObject jo) {
        String code = (String)jo.get("code");
        String classCode = (String)jo.get("classCode");
        JSONArray jsonArray = (JSONArray)jo.get("prerequisites");
        try {
            for (Object o : jsonArray) {
                PrerequisiteDAO prerequisiteDAO = new PrerequisiteDAO(code, classCode, (String) o);
                PrerequisiteRepository.getInstance().insert(prerequisiteDAO);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void addOffering(JSONObject jo) {
        String code = (String)jo.get("code");
        String classCode = (String)jo.get("classCode");
        String name = (String)jo.get("name");
        int units = ((Long)jo.get("units")).intValue();
        String instructor = (String)jo.get("instructor");
        int capacity = ((Long) jo.get("capacity")).intValue();
        String type = (String)jo.get("type");
        JSONObject classTime = (JSONObject)jo.get("classTime");
        String time = (String)classTime.get("time");
        String classTimeStart = time.substring(0, time.indexOf("-"));
        classTimeStart = (classTimeStart.length() < 5) ? "0" + classTimeStart : classTimeStart;
        String classTimeEnd = time.substring(time.indexOf("-") + 1);
        classTimeEnd = (classTimeEnd.length() < 5) ? "0" + classTimeEnd : classTimeEnd;
        JSONObject examTime = (JSONObject)jo.get("examTime");
        String examTimeStart = (String)examTime.get("start");
        String examTimeEnd = (String)examTime.get("end");
        JSONArray jsonArray = (JSONArray)classTime.get("days");
        CourseDAO courseDAO = new CourseDAO(code, classCode, name, instructor, units, type, classTimeStart ,classTimeEnd, examTimeStart, examTimeEnd, capacity);
        try {
            CourseRepository.getInstance().insert(courseDAO);
            for (Object o : jsonArray) {
                ClassDayDAO classDayDAO = new ClassDayDAO(code, classCode, (String) o);
                ClassDayRepository.getInstance().insert(classDayDAO);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
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
        String email = (String)jo.get("email");
        String password = (String)jo.get("password");
        String birthDate = (String)jo.get("birthDate");
        String field = (String)jo.get("field");
        String faculty = (String)jo.get("faculty");
        String level = (String)jo.get("level");
        String status = (String)jo.get("status");
        String img = (String)jo.get("img");
        StudentDAO studentDAO = new StudentDAO(id, name, secondName, email, password, birthDate, field, faculty, level, status, img);
        try {
            StudentRepository.getInstance().insert(studentDAO);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public ArrayList<String> getStudentsId() {
        try {
            return StudentRepository.getInstance().getAllIds();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return new ArrayList<String>();
        }
    }

    public StudentDAO findStudent(String id) throws StudentNotFound, SQLException {
        return StudentRepository.getInstance().findById(id);
    }

    public Course findCourse(String code, String classCode) throws OfferingNotFound {
        for(Course course: courses)
            if(course.getCode().equals(code) && course.getClassCode().equals(classCode))
                return course;
        throw new OfferingNotFound();
    }
}