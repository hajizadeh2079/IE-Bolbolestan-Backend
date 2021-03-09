import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void findCourseTest() throws Exception {
        Course course = unitSelectionSystem.findCourse("8101005", "01");
        String expected = "Calculus 2";
        String actual = course.getName();
        assertEquals(expected, actual);
        assertThrows(OfferingNotFound.class, () -> {
            unitSelectionSystem.findCourse("8101005", "02");
        });
    }

    @Test
    public void findStudentTest() throws Exception {
        Student student = unitSelectionSystem.findStudent("810197227");
        String expected = "Karimi";
        String actual = student.getSecondName();
        assertEquals(expected, actual);
        assertThrows(StudentNotFound.class, () -> {
            unitSelectionSystem.findStudent("810197452");
        });
    }

    @Test
    public void addToWeeklyScheduleTest() throws Exception {
        unitSelectionSystem.addToWeeklySchedule("810196285", "8101010", "01");
        Student student = unitSelectionSystem.findStudent("810196285");
        WeeklySchedule weeklySchedule = student.getWeeklySchedule();
        String expected = "Discrete Mathematics";
        String actual = weeklySchedule.getCourses().get(0).getName();
        assertEquals(expected, actual);
        assertThrows(ClassTimeCollisionError.class, () -> {
            unitSelectionSystem.addToWeeklySchedule("810196285", "8101021", "01");
        });
        unitSelectionSystem.addToWeeklySchedule("810195115", "8101010", "01");
        assertThrows(ExamTimeCollisionError.class, () -> {
            unitSelectionSystem.addToWeeklySchedule("810195115", "8101011", "01");
        });
        assertThrows(PrerequisitesError.class, () -> {
            unitSelectionSystem.addToWeeklySchedule("810196285", "8101011", "01");
        });
        assertThrows(PrerequisitesError.class, () -> {
            unitSelectionSystem.addToWeeklySchedule("810196285", "8101005", "01");
        });
    }
}