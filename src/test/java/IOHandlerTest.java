import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IOHandlerTest {
    IOHandler handleIO;
    UnitSelectionSystem unitSelectionSystem;

    @BeforeEach
    public void setup() {
        handleIO = new IOHandler(new HtmlPages());
        unitSelectionSystem = new UnitSelectionSystem();
    }

    @Test
    public void testGetData() {
        JSONArray jsonArray;
        jsonArray = handleIO.getData("http://138.197.181.131:5000/api/courses");
        unitSelectionSystem.addOfferings(jsonArray);
        jsonArray = handleIO.getData("http://138.197.181.131:5000/api/students");
        unitSelectionSystem.addStudents(jsonArray);
        try {
            Course course = unitSelectionSystem.findCourse("8101002", "01");
            String expected = "Algorithm Design 1";
            String actual = course.getName();
            assertEquals(expected, actual);
            expected = "01";
            actual = course.getClassCode();
            assertEquals(expected, actual);

            Student student = unitSelectionSystem.findStudent("810196668");
            expected = "Bahrami";
            actual = student.getSecondName();
            assertEquals(expected, actual);
            expected = "1378/07/26";
            actual = student.getBirthDate();
            assertEquals(expected, actual);
        } catch (Exception ignored) { }
    }
}