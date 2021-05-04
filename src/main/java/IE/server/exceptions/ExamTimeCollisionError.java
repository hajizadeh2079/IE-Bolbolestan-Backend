package IE.server.exceptions;

public class ExamTimeCollisionError extends Exception {
    public ExamTimeCollisionError(String code1, String classCode1, String code2, String classCode2) {
        super("تداخل زمانی امتحان:  " + classCode1 + "-" + code1 + " با " + classCode2 + "-" + code2);
    }
}
