import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class UnitSelectionSystemTest {
    UnitSelectionSystem unitSelectionSystem;
    @BeforeEach
    public void setup() {
        unitSelectionSystem = new UnitSelectionSystem();
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
}