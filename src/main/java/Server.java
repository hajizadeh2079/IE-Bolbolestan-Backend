import io.javalin.Javalin;
import org.json.simple.JSONArray;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.*;

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
        app.routes(() -> {
            path("", () -> {
                get(ctx -> ctx.result("Unit Selection System!"));
            });
            path("courses", () -> {
                get(ctx -> ctx.html(ioHandler.htmlPageHandler("courses", unitSelectionSystem.getCourses())));
            });
            path("course", () -> {
                path(":course_id/:class_code", () -> {
                    get(ctx -> ctx.html(ioHandler.htmlPageHandler("course",
                            unitSelectionSystem.findCourse(ctx.pathParam("course_id"), ctx.pathParam("class_code")))));
                });
            });
        });
        app.exception(OfferingNotFound.class, (e, ctx) -> {
            ctx.result("Course not found!");
            ctx.status(404);
        });
    }
}