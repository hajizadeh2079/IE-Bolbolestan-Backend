import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.json.simple.JSONObject;
import java.util.ArrayList;

public class UnitSelectionSystemTest {
    UnitSelectionSystem unitSelectionSystem;

    @BeforeEach
    public void setup() {
        unitSelectionSystem = new UnitSelectionSystem();
    }

    @Test
    public void testGetOfferings() {

    }

    @Test
    public void testAddOffering() {
        ArrayList<Course> courses;
        JSONObject course1 = new JSONObject();
        course1.put("code", "81013602");
        course1.put("name", "Internet Engineering");
        course1.put("instructor", "Ehsan Khamespanah");
        course1.put("units", 3);
        JSONObject classTime = new JSONObject();
        JSONArray classDays = new JSONArray();
        classDays.add("saturday");
        classDays.add("monday");
        classTime.put("days", classDays);
        classTime.put("time", "16:00-17:30");
        course1.put("classTime", classTime);
        JSONObject examTime = new JSONObject();
        examTime.put("start", "2021-09-01T08:00:00");
        examTime.put("end", "2021-09-01T08:00:00");
        course1.put("examTime", examTime);
        course1.put("capacity", 60);
        JSONArray prerequisites = new JSONArray();
        prerequisites.add("Advanced Programming");
        prerequisites.add("Operating Systems");
        course1.put("prerequisites", prerequisites);
        unitSelectionSystem.addOffering(course1);
        courses = unitSelectionSystem.getCourses();
        assertEquals("16:00", courses.get(0).getClassTimeStart().toString());
    }

    @Test
    public void testAddStudent() {
        ArrayList<Student> students;
        JSONObject std1 = new JSONObject();
        std1.put("studentId", "810197452");
        std1.put("name", "Armin Afsharian");
        std1.put("enteredAt", "1397");
        unitSelectionSystem.addStudent(std1);
        students = unitSelectionSystem.getStudents();
        assertEquals("810197452", students.get(0).getId());
    }

    @Test
    public void testFindStudent() {
        Student student;
        JSONObject std1 = new JSONObject();
        std1.put("studentId", "810197452");
        std1.put("name", "Armin Afsharian");
        std1.put("enteredAt", "1397");
        unitSelectionSystem.addStudent(std1);
        try {
            student = unitSelectionSystem.findStudent("810197452");
            assertEquals("810197452", student.getId());
        }catch (Exception exception) {}
        Exception exception = assertThrows(StudentNotFound.class, () -> {
            unitSelectionSystem.findStudent("810197462");
        });
        String expectedMessage = "StudentNotFound";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}