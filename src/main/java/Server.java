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
        for (Student student: unitSelectionSystem.getStudents()) {
            jsonArray = ioHandler.getData("http://138.197.181.131:5000/api/grades/" + student.getId());
            student.setReportCard(new ReportCard(jsonArray, unitSelectionSystem.getCodesUnits()));
        }
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
                    post(ctx -> {
                        unitSelectionSystem.addToWeeklySchedule(ctx.formParam("std_id"),
                                ctx.pathParam("course_id"), ctx.pathParam("class_code"));
                        ctx.result("Course added successfully!");
                    });
                });
            });
            path("change_plan", () -> {
                path(":student_id", () -> {
                    get(ctx -> ctx.html(ioHandler.htmlPageHandler("change_plan",
                            unitSelectionSystem.findStudent(ctx.pathParam("student_id")))));
                    post(ctx -> {
                        unitSelectionSystem.removeFromWeeklySchedule(ctx.pathParam("student_id"),
                                ctx.formParam("course_code"), ctx.formParam(("class_code")));
                        ctx.result("Course removed successfully!");
                    });
                });
            });
            path("plan", () -> {
                path(":student_id", () -> {
                    get(ctx -> ctx.html(ioHandler.htmlPageHandler("plan",
                            unitSelectionSystem.findStudent(ctx.pathParam("student_id")))));
                });
            });
            path("profile", () -> {
                path(":student_id", () -> {
                    get(ctx -> ctx.html(ioHandler.htmlPageHandler("profile",
                            unitSelectionSystem.findStudent(ctx.pathParam("student_id")))));
                });
            });
        });
        app.exception(OfferingNotFound.class, (e, ctx) -> {
            ctx.result("Course not found!");
            ctx.status(404);
        });

        app.exception(StudentNotFound.class, (e, ctx) -> {
            ctx.result("Student not found!");
            ctx.status(404);
        });

        app.exception(ExamTimeCollisionError.class, (e, ctx) -> {
            ctx.result("Exam time collision error!");
            ctx.status(200);
        });

        app.exception(ClassTimeCollisionError.class, (e, ctx) -> {
            ctx.result("Class time collision error!");
            ctx.status(200);
        });
    }
}