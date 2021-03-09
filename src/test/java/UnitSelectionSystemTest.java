import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;

public class UnitSelectionSystemTest {
    IOHandler ioHandler;
    UnitSelectionSystem unitSelectionSystem;

    @BeforeEach
    public void setup() {
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
}
