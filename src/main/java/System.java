import java.util.ArrayList;
import org.json.simple.JSONObject;

public class System {
    private ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Course> courses = new ArrayList<Course>();
    public void addStudent(JSONObject jo){
        String id = (String)jo.get("studentId");
        String name = (String)jo.get("name");
        String enteredAt = (String)jo.get("enteredAt");
        Student student = new Student(id, name, enteredAt);
        students.add(student);
    }
}
