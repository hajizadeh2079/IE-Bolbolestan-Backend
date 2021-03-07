public class Main {

    public static void main(String[] args) {
        Server server = new Server();
        server.prepareData();
        server.addPaths();
        server.start(8000);
    }
}