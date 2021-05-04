package IE.server.exceptions;

public class ClassTimeCollisionError extends Exception {
    public ClassTimeCollisionError(String code1, String classCode1, String code2, String classCode2) {
        super("تداخل زمانی كلاس: " + classCode1 + "-" + code1 + " با " + classCode2 + "-" + code2);
    }
}
