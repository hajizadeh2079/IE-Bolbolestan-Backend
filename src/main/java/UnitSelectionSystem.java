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

    public void addOffering(JSONObject jo) {
        String code = (String)jo.get("code");
        String name = (String)jo.get("name");
        String instructor = (String)jo.get("instructor");
        int units = (Integer)jo.get("units");
        JSONObject classTime = (JSONObject)jo.get("classTime");
        JSONObject examTime = (JSONObject)jo.get("examTime");
        int capacity = (Integer)jo.get("capacity");
        JSONArray prerequisites = (JSONArray)jo.get("prerequisites");
        Course course = new Course(code, name, instructor, units, classTime, examTime, capacity, prerequisites);
        int index = 0;
        for (index = 0; index < courses.size(); index++)
            if (name.equals(courses.get(index).getName()))
                break;
        courses.add(index, course);
    }

    public void addStudent(JSONObject jo) {
        String id = (String)jo.get("studentId");
        String name = (String)jo.get("name");
        String enteredAt = (String)jo.get("enteredAt");
        Student student = new Student(id, name, enteredAt);
        students.add(student);
    }

    public void getOffering(JSONObject jo) {
        String studentId = (String)jo.get("studentId");
        String code = (String)jo.get("code");
        Student student;
        Course course;
        try {
            student = findStudent(studentId);
            try { //todo
                course = findCourse(code);

            } catch (Exception offeringNotFound) {
                    printResponse(false, offeringNotFound);
            }
        } catch (Exception studentNotFound) {
                printResponse(false, studentNotFound);
        }
    }

    public void printResponse(boolean success, Object data) {
        JSONObject response = new JSONObject();
        if(success) { //todo

        } else {
            response.put("success", false);
            response.put("error", ((Exception)data).getMessage());
        }
        System.out.print(response);
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
