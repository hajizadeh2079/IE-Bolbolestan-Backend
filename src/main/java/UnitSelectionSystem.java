import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UnitSelectionSystem {
    private ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Course> courses = new ArrayList<Course>();

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public JSONObject doCommand(String command, JSONObject jo) {
        return switch (command) {
            case "addStudent" -> addStudent(jo);
            case "addOffering" -> addOffering(jo);
            case "getOfferings" -> getOfferings(jo);
            case "getOffering" -> getOffering(jo);
            case "addToWeeklySchedule" -> addToWeeklySchedule(jo);
            case "getWeeklySchedule" -> getWeeklySchedule(jo);
            case "removeFromWeeklySchedule" -> removeFromWeeklySchedule(jo);
            case "finalize" -> finalize(jo);
            default -> null;
        };
    }

    public JSONObject finalize(JSONObject jo) {
        String studentId = (String) jo.get("studentId");
        try {
            Student student = findStudent(studentId);
            try {
                checkForUnitsError(student.studentUnitsCount());
                try {
                    checkForCapacityError(student);
                    try {
                        checkForClassTimeCollisionError(student);
                        return null;
                    } catch (Exception classTimeCollisionError) {
                        return createResponse(false, classTimeCollisionError);
                    }
                } catch (Exception capacityError) {
                    return createResponse(false, capacityError);
                }
            } catch (Exception unitsMinOrMaxError) {
                return createResponse(false, unitsMinOrMaxError);
            }
        } catch (Exception studentNotFound) {
            return createResponse(false, studentNotFound);
        }
    }

    public void checkForUnitsError(int unitsCount) throws UnitsMinOrMaxError {
        if (unitsCount < 12)
            throw new UnitsMinOrMaxError("Minimum");
        else if (unitsCount > 20)
            throw new UnitsMinOrMaxError("Maximum");
    }

    public void checkForCapacityError(Student student) throws CapacityError {
        for (Course course: student.getNonFinalizedCourses())
            if (course.getCapacity() == course.getRemainingCapacity())
                throw new CapacityError(course.getCode());
    }

    public void checkForClassTimeCollisionError(Student student) throws ClassTimeCollisionError {
        ArrayList<Course> finalizedCourses = student.getFinalizedCourses();
        ArrayList<Course> nonFinalizedCourses = student.getNonFinalizedCourses();
        for (Course course1 : nonFinalizedCourses) {
            for (Course course2 : nonFinalizedCourses) {
                if (course1.getCode().equals(course2.getCode()))
                    continue;
                if(checkForCollisionClassTime(course1, course2))
                    throw new ClassTimeCollisionError(course1.getCode(), course2.getCode());
            }
        }
        for (Course course1 : nonFinalizedCourses) {
            for (Course course2 : finalizedCourses) {
                if(checkForCollisionClassTime(course1, course2))
                    throw new ClassTimeCollisionError(course1.getCode(), course2.getCode());
            }
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
            try {
                int start1ToSeconds = course1.getClassTimeStart().toSecondOfDay();
                int end1ToSeconds = course1.getClassTimeEnd().toSecondOfDay();
                int start2ToSeconds = course2.getClassTimeStart().toSecondOfDay();
                int end2ToSeconds = course2.getClassTimeEnd().toSecondOfDay();
                return start1ToSeconds < end2ToSeconds && start2ToSeconds < end1ToSeconds;
            } catch (Exception parseException) {}
        }
        return false;
    }

    public JSONObject getWeeklySchedule(JSONObject jo) {
        String studentId = (String) jo.get("studentId");
        try {
            Student student = findStudent(studentId);
            JSONObject data = new JSONObject();
            JSONArray items = new JSONArray();
            ArrayList<Course> finalizedCourses = student.getFinalizedCourses();
            ArrayList<Course> nonFinalizedCourses = student.getNonFinalizedCourses();
            for (Course course: finalizedCourses) {
                JSONObject item = new JSONObject();
                item.put("code", course.getCode());
                item.put("name", course.getName());
                item.put("instructor", course.getInstructor());
                item.put("classTime", course.getClassTime());
                item.put("examTime", course.getExamTime());
                item.put("status", "finalized");
                items.add(item);
            }
            for (Course course: nonFinalizedCourses) {
                JSONObject item = new JSONObject();
                item.put("code", course.getCode());
                item.put("name", course.getName());
                item.put("instructor", course.getInstructor());
                item.put("classTime", course.getClassTime());
                item.put("examTime", course.getExamTime());
                item.put("status", "non-finalized");
                items.add(item);
            }
            data.put("weeklySchedule", items);
            return createResponse(true, data);
        } catch (Exception studentNotFound) {
            return createResponse(false, studentNotFound);
        }
    }

    public JSONObject addToWeeklySchedule(JSONObject jo) {
        String studentId = (String)jo.get("studentId");
        String code = (String)jo.get("code");
        try {
            Student student = findStudent(studentId);
            try {
                Course newCourse = findCourse(code);
                ArrayList<Course> finalizedCourses = student.getFinalizedCourses();
                ArrayList<Course> nonFinalizedCourses = student.getNonFinalizedCourses();
                for (Course course: finalizedCourses)
                    if (course.getCode().equals(newCourse.getCode()))
                        return null;
                for (Course course: nonFinalizedCourses)
                    if (course.getCode().equals(newCourse.getCode()))
                        return null;
                nonFinalizedCourses.add(newCourse);
                student.setNonFinalizedCourses(nonFinalizedCourses);
                return createResponse(true, new JSONObject());
            } catch (Exception offeringNotFound) {
                return createResponse(false, offeringNotFound);
            }
        } catch (Exception studentNotFound) {
            return createResponse(false, studentNotFound);
        }
    }

    public JSONObject removeFromWeeklySchedule(JSONObject jo) {
        String studentId = (String)jo.get("studentId");
        String code = (String)jo.get("code");
        try {
            Student student = findStudent(studentId);
            try {
                Course courseToBeRemoved = findCourse(code);
                try {
                    String listToBeSearched = student.isCourseExist(code);
                    if(listToBeSearched.equals("finalized")) {
                        ArrayList<Course> finalizedCourses = student.getFinalizedCourses();
                        finalizedCourses.removeIf(course -> course.getCode().equals(courseToBeRemoved.getCode()));
                        student.setFinalizedCourses(finalizedCourses);
                    }
                    else if(listToBeSearched.equals("notFinalized")) {
                        ArrayList<Course> nonFinalizedCourses = student.getNonFinalizedCourses();
                        nonFinalizedCourses.removeIf(course -> course.getCode().equals(courseToBeRemoved.getCode()));
                        student.setNonFinalizedCourses(nonFinalizedCourses);
                    }
                    return createResponse(true, new JSONObject());

                } catch (Exception offeringNotFound) {
                    return createResponse(false, offeringNotFound);
                }
            } catch (Exception offeringNotFound) {
                return createResponse(false, offeringNotFound);
            }
        } catch (Exception studentNotFound) {
            return createResponse(false, studentNotFound);
        }
    }

    public JSONObject addOffering(JSONObject jo) {
        String code = (String)jo.get("code");
        String name = (String)jo.get("name");
        String instructor = (String)jo.get("instructor");
        long units = (Long)jo.get("units");
        JSONObject classTime = (JSONObject)jo.get("classTime");
        JSONObject examTime = (JSONObject)jo.get("examTime");
        long capacity = (Long) jo.get("capacity");
        JSONArray prerequisites = (JSONArray)jo.get("prerequisites");
        Course course = new Course(code, name, instructor, units, classTime, examTime, capacity, prerequisites);
        int index = 0;
        for (index = 0; index < courses.size(); index++)
            if (name.equals(courses.get(index).getName()))
                break;
        courses.add(index, course);
        return createResponse(true, new JSONObject());
    }

    public JSONObject addStudent(JSONObject jo) {
        String id = (String)jo.get("studentId");
        String name = (String)jo.get("name");
        String enteredAt = (String)jo.get("enteredAt");
        Student student = new Student(id, name, enteredAt);
        students.add(student);
        return createResponse(true, new JSONObject());
    }

    public JSONObject getOfferings(JSONObject jo) {
        String studentId = (String)jo.get("studentId");
        Student student;
        try {
            student = findStudent(studentId);
            JSONArray data = new JSONArray();
            for (Course course: courses) {
                JSONObject item = new JSONObject();
                item.put("code", course.getCode());
                item.put("name", course.getName());
                item.put("instructor", course.getInstructor());
                data.add(item);
            }
            return createResponse(true, data);
        } catch (Exception studentNotFound) {
            return createResponse(false, studentNotFound);
        }
    }

    public JSONObject getOffering(JSONObject jo) {
        String studentId = (String)jo.get("studentId");
        String code = (String)jo.get("code");
        Student student;
        Course course;
        try {
            student = findStudent(studentId);
            try {
                course = findCourse(code);
                JSONObject data = new JSONObject();
                data.put("code", course.getCode());
                data.put("name", course.getName());
                data.put("instructor", course.getInstructor());
                data.put("units", course.getUnits());
                data.put("classTime", course.getClassTime());
                data.put("examTime", course.getExamTime());
                data.put("capacity", course.getCapacity());
                data.put("prerequisites", course.getPrerequisites());
                return createResponse(true, data);
            } catch (Exception offeringNotFound) {
                return createResponse(false, offeringNotFound);
            }
        } catch (Exception studentNotFound) {
            return createResponse(false, studentNotFound);
        }
    }

    public JSONObject createResponse(boolean success, Object data) {
        JSONObject response = new JSONObject();
        if(success) {
            response.put("success", true);
            if (data instanceof JSONObject)
                response.put("data", (JSONObject) data);
            else if (data instanceof JSONArray)
                response.put("data", (JSONArray) data);
        } else {
            response.put("success", false);
            response.put("error", ((Exception)data).getMessage());
        }
        return response;
    }

    public Student findStudent(String id)  throws StudentNotFound {
        for(Student student: students)
            if(student.getId().equals(id))
                return student;
        throw new StudentNotFound();
    }

    public Course findCourse(String code)  throws OfferingNotFound {
        for(Course course: courses)
            if(course.getCode().equals(code))
                return course;
        throw new OfferingNotFound();
    }
}