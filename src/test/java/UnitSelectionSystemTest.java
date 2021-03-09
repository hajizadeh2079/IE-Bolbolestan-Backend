import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void profileTest() throws Exception {
        Student student = unitSelectionSystem.findStudent("810196285");
        int expected_tpu = 40;
        int actual_tpu = student.getReportCard().calcTPU();
        assertEquals(expected_tpu, actual_tpu);
        double expected_gpu = 681.0 / 52;
        double actual_gpu = student.getReportCard().calcGPA();
        assertEquals(expected_gpu, actual_gpu);
    }

    @Test
    public void changePlanTest() throws Exception {
        Student student = unitSelectionSystem.findStudent("810196285");
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101001", "01"));
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101002", "01"));
        student.removeFromWeeklySchedule(unitSelectionSystem.findCourse("8101001", "01"));
        String expected = "8101002_01";
        String actual = student.getWeeklySchedule().getCourses().get(0).getCode() + "_" +
                student.getWeeklySchedule().getCourses().get(0).getClassCode();
        assertEquals(expected, actual);
    }

    @Test
    public void submitTest() throws Exception {
        Student student = unitSelectionSystem.findStudent("810196285");
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101001", "01"));
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101002", "01"));
        assertFalse(unitSelectionSystem.finalize(student.getId()));
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101003", "01"));
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101004", "01"));
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101010", "01"));
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101013", "01"));
        student.addToWeeklySchedule(unitSelectionSystem.findCourse("8101030", "01"));
        assertFalse(unitSelectionSystem.finalize(student.getId()));
        student.removeFromWeeklySchedule(unitSelectionSystem.findCourse("8101030", "01"));
        assertTrue(unitSelectionSystem.finalize(student.getId()));
    }
}