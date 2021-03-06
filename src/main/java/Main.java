import io.javalin.Javalin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create();
        Server server = new Server(app);
        server.start(8000);
        server.addPaths();
    }
}