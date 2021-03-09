import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnitSelectionSystemTest {
    IOHandler ioHandler;
    UnitSelectionSystem unitSelectionSystem;

    @BeforeEach
    public void setup() {
        ioHandler = new IOHandler(new HtmlPages());
        unitSelectionSystem = new UnitSelectionSystem();
        JSONArray jsonArray;
        jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/courses");
        unitSelectionSystem.addOfferings(jsonArray);
        jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/students");
        unitSelectionSystem.addStudents(jsonArray);
        for (Student student: unitSelectionSystem.getStudents()) {
            jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/grades/" + student.getId());
            student.setReportCard(new ReportCard(jsonArray, unitSelectionSystem.getCodesUnits()));
        }
    }

    @Test
    public void findCourseTest() throws Exception{
        Course course = unitSelectionSystem.findCourse("8101005", "01");
        String expected = "Calculus 2";
        String actual = course.getName();
        assertEquals(expected, actual);
        assertThrows(OfferingNotFound.class, () -> {
            unitSelectionSystem.findCourse("8101005", "02");
        });
    }

    @Test
    public void findStudentTest() throws Exception{
        Student student = unitSelectionSystem.findStudent("810197227");
        String expected = "Karimi";
        String actual = student.getSecondName();
        assertEquals(expected, actual);
        assertThrows(StudentNotFound.class, () -> {
            unitSelectionSystem.findStudent("810197452");
        });
    }
}
