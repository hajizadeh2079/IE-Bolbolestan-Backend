package IE.server.exceptions;

public class AlreadyPassedError extends Exception {
    public AlreadyPassedError(String code) {
        super("عدم امكان اخذ مجدد درس گذرانده شده:  " + code);
    }
}
