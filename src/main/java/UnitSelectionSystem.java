import java.util.ArrayList;
import org.json.simple.JSONObject;

public class UnitSelectionSystem {
    private ArrayList<Student> students = new ArrayList<Student>();
    private ArrayList<Course> courses = new ArrayList<Course>();
    public void addStudent(JSONObject jo){
        String id = (String)jo.get("studentId");
        String name = (String)jo.get("name");
        String enteredAt = (String)jo.get("enteredAt");
        Student student = new Student(id, name, enteredAt);
        students.add(student);
    }
    public void getOffering(JSONObject jo){
        String studentId = (String)jo.get("studentId");
        String code = (String)jo.get("code");
    }
    public Student findStudent(String id){
        for(Student student: students)
            if(student.getId().equals(id))
                return student;
        return null;
    }
}
