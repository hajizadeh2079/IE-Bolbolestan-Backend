import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class UnitSelectionSystemTest {
    UnitSelectionSystem unitSelectionSystem;

    @BeforeEach
    public void setup() {
        unitSelectionSystem = new UnitSelectionSystem();
        File file = new File("src/test/resources/prepareForTest.txt");
        try {
            Scanner scanner = new Scanner(file);
            JSONParser parser = new JSONParser();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line == null)
                    continue;
                String command = line.substring(0, line.indexOf(" "));
                String jsonString = line.substring(line.indexOf(" ") + 1);
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                JSONObject response = unitSelectionSystem.doCommand(command, jsonObject);
            }
        } catch (FileNotFoundException | ParseException exception) { }
    }

    @Test
    public void testGetWeeklySchedule() {
        JSONObject course = new JSONObject();
        course.put("studentId", "810197100");
        course.put("code", "81010080");
        JSONObject response = unitSelectionSystem.addToWeeklySchedule(course);
        course.put("code", "81010070");
        response = unitSelectionSystem.addToWeeklySchedule(course);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", "810197100");
        response = unitSelectionSystem.getWeeklySchedule(jsonObject);
        int expected = 2;
        int actual = ((JSONArray)((JSONObject)response.get("data")).get("weeklySchedule")).size();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddToWeeklySchedule() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", "810197100");
        jsonObject.put("code", "81010080");
        JSONObject response = unitSelectionSystem.addToWeeklySchedule(jsonObject);
        String expected = "Engineering Mathematics";
        String actual;
        try {
            ArrayList<Course> nonFinalizedCourses = unitSelectionSystem.findStudent("810197100").getNonFinalizedCourses();
            actual = nonFinalizedCourses.get(nonFinalizedCourses.size() - 1).getName();
            assertEquals(expected, actual);
        } catch (StudentNotFound exception) {}
        jsonObject.put("studentId", "810197100");
        jsonObject.put("code", "81019999");
        response = unitSelectionSystem.addToWeeklySchedule(jsonObject);
        expected = "OfferingNotFound";
        actual = (String) response.get("error");
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOfferings() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", "810197100");
        JSONObject response = unitSelectionSystem.getOfferings(jsonObject);
        String expected = "Internet Engineering";
        String actual = (String) ((JSONObject)(((JSONArray)response.get("data")).get(1))).get("name");
        assertEquals(expected, actual);
        jsonObject.put("studentId", "810197999");
        response = unitSelectionSystem.getOfferings(jsonObject);
        expected = "StudentNotFound";
        actual = (String) response.get("error");
        assertEquals(expected, actual);
    }

    @Test
    public void testAddOffering() {
        ArrayList<Course> courses;
        JSONObject course1 = new JSONObject();
        course1.put("code", "81013602");
        course1.put("name", "Unknown");
        course1.put("instructor", "Ehsan Khamespanah");
        course1.put("units", 3L);
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
        course1.put("capacity", 60L);
        JSONArray prerequisites = new JSONArray();
        prerequisites.add("Advanced Programming");
        prerequisites.add("Operating Systems");
        course1.put("prerequisites", prerequisites);
        JSONObject response = unitSelectionSystem.addOffering(course1);
        courses = unitSelectionSystem.getCourses();
        assertEquals("16:00", courses.get(courses.size() - 1).getClassTimeStart().toString());
    }

    @Test
    public void testAddStudent() {
        ArrayList<Student> students;
        JSONObject std1 = new JSONObject();
        std1.put("studentId", "810197452");
        std1.put("name", "Armin Afsharian");
        std1.put("enteredAt", "1397");
        JSONObject response = unitSelectionSystem.addStudent(std1);
        students = unitSelectionSystem.getStudents();
        assertEquals("810197452", students.get(students.size() - 1).getId());
    }

    @Test
    public void testFindStudent() {
        Student student;
        JSONObject std1 = new JSONObject();
        std1.put("studentId", "810197300");
        std1.put("name", "Javad");
        std1.put("enteredAt", "1397");
        JSONObject response = unitSelectionSystem.addStudent(std1);
        try {
            student = unitSelectionSystem.findStudent("810197300");
            assertEquals("Javad", student.getName());
        } catch (Exception exception) {}
        Exception exception = assertThrows(StudentNotFound.class, () -> {
            unitSelectionSystem.findStudent("810197462");
        });
        String expectedMessage = "StudentNotFound";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetOffering() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("studentId", "810197100");
        jsonObject.put("code", "81010030");
        JSONObject response = unitSelectionSystem.getOffering(jsonObject);
        String expected = "Artificial Intelligence";
        String actual = (String) (((JSONObject)response.get("data")).get("name"));
        assertEquals(expected, actual);
        jsonObject.put("studentId", "810197999");
        jsonObject.put("code", "81010030");
        response = unitSelectionSystem.getOffering(jsonObject);
        expected = "StudentNotFound";
        actual = (String) response.get("error");
        assertEquals(expected, actual);
        jsonObject.put("studentId", "810197100");
        jsonObject.put("code", "81010130");
        response = unitSelectionSystem.getOffering(jsonObject);
        expected = "OfferingNotFound";
        actual = (String) response.get("error");
        assertEquals(expected, actual);
    }
}