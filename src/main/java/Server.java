import io.javalin.Javalin;
import org.json.simple.JSONArray;

public class Server {
    private Javalin app;
    private IOHandler ioHandler;
    private UnitSelectionSystem unitSelectionSystem;

    public Server() {
        app = Javalin.create();
        ioHandler = new IOHandler(new HtmlPages());
        unitSelectionSystem = new UnitSelectionSystem();
    }

    public void start(int port) {
        app.start(port);
    }

    public void prepareData() {
        JSONArray jsonArray;
        jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/courses");
        unitSelectionSystem.addOfferings(jsonArray);
        jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/students");
        unitSelectionSystem.addStudents(jsonArray);
    }

    public void addPaths() {
        app.get("/", ctx -> ctx.result("Unit Selection System!"));
        app.get("/courses",
                ctx -> ctx.html(ioHandler.htmlPageHandler("courses", null))
        );
    }
}