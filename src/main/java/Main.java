import io.javalin.Javalin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        HandleIO handleIO = new HandleIO();
        UnitSelectionSystem unitSelectionSystem = new UnitSelectionSystem();
        JSONArray jsonArray;
        jsonArray = handleIO.getCoursesData("http://138.197.181.131:5000/api/courses");
        unitSelectionSystem.addOfferings(jsonArray);
        jsonArray = handleIO.getStudentsData("http://138.197.181.131:5000/api/students");
        unitSelectionSystem.addStudents(jsonArray);
        //Javalin app = Javalin.create();
        //Server server = new Server(app);
        //server.start(8000);
        //server.addPaths();

    }
}