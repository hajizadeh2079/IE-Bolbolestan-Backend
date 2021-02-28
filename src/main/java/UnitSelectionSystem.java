import java.util.ArrayList;
import org.json.simple.JSONObject;

public class UnitSelectionSystem {
    private ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Course> courses = new ArrayList<Course>();

    public ArrayList<Student> getStudents() {
        return students;
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
            try {
                course = findCourse(code);

            } catch (Exception offeringNotFound) {

            }
        } catch (Exception studentNotFound) {

        }
    }

    public Student findStudent(String id)  throws StudentNotFound{
        for(Student student: students)
            if(student.getId().equals(id))
                return student;
        throw new StudentNotFound();
    }

    public Course findCourse(String code)  throws OfferingNotFound{
        for(Course course: courses)
            if(course.getCode().equals(code))
                return course;
        throw new OfferingNotFound();
    }
}
