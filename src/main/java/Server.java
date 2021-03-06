import io.javalin.Javalin;


public class Server {
    private Javalin app;
    private HandleIO handleIO;

    public Server(Javalin app, HandleIO handleIO) {
        this.app = app;
        this.handleIO = handleIO;
    }

    public void start(int port) {
        app.start(port);
    }

    public void addPaths() {
        app.get("/", ctx -> ctx.result("Unit Selection System!!"));
        app.get("/courses", ctx -> ctx.html(handleIO.htmlPageHandler("courses", null)));
    }
}
