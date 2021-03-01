import java.util.ArrayList;
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
        switch (command) {
            case "addStudent":
                return addStudent(jo);
            case "addOffering":
                return addOffering(jo);
            case "getOfferings":
                return getOfferings(jo);
            case "getOffering":
                return getOffering(jo);
            case "addToWeeklySchedule":
                return addToWeeklySchedule(jo);
            default:
                return null;
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
            response.put("success", false);
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