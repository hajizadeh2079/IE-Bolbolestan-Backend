import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandelIOTest {
    HandleIO handleIO;
    UnitSelectionSystem unitSelectionSystem;
    HtmlPages htmlPages;

    @BeforeEach
    public void setup() {
        htmlPages = new HtmlPages();
        handleIO = new HandleIO(htmlPages);
        unitSelectionSystem = new UnitSelectionSystem();
    }

    @Test
    public void testGetCoursesData() {
        JSONArray jsonArray;
        jsonArray = handleIO.getCoursesData("http://138.197.181.131:5000/api/courses");
        unitSelectionSystem.addOfferings(jsonArray);
        try {
            Course course = unitSelectionSystem.findCourse("8101002");
            String expected = "Algorithm Design 1";
            String actual = course.getName();
            assertEquals(expected, actual);
            expected = "01";
            actual = course.getClassCode();
            assertEquals(expected, actual);
        } catch (Exception ignored) {}
    }

    @Test
    public void testGetStudentsData() {
        JSONArray jsonArray;
        jsonArray = handleIO.getStudentsData("http://138.197.181.131:5000/api/students");
        unitSelectionSystem.addStudents(jsonArray);
        try {
            Student student = unitSelectionSystem.findStudent("810196668");
            String expected = "Bahrami";
            String actual = student.getSecondName();
            assertEquals(expected, actual);
            expected = "1378/07/26";
            actual = student.getBirthDate();
            assertEquals(expected, actual);
        } catch (Exception ignored) {}
    }
}
