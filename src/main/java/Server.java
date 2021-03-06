import io.javalin.Javalin;


public class Server {
    private Javalin app;

    public Server(Javalin app) {
        this.app = app;
    }

    public void start(int port) {
        app.start(port);
    }

    public void addPaths() {
        app.get("/", ctx -> ctx.result("HelloWorld"));
    }
}
