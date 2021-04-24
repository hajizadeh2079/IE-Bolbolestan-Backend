package IE.server.services;

public class Exceptions extends Exception {
    public Exceptions(String s) {
        super(s);
    }
}

class StudentNotFound extends Exceptions {
    public StudentNotFound() {
        super("StudentNotFound");
    }
}

class OfferingNotFound extends Exceptions {
    public OfferingNotFound() {
        super("OfferingNotFound");
    }
}

class UnitsMinOrMaxError extends Exceptions {
    public UnitsMinOrMaxError(String MinOrMax) {
        super(MinOrMax.equals("Min") ? "عدم رعايت حداقل تعداد واحد" : "عدم رعايت حداكثر تعداد واحد");
    }
}

class CapacityError extends Exceptions {
    public CapacityError(String code, String classCode) {
        super("CapacityError " + code + "-" + classCode);
    }
}

class ClassTimeCollisionError extends Exceptions {
    public ClassTimeCollisionError(String code1, String classCode1, String code2, String classCode2) {
        super("تداخل زمانی كلاس: " + classCode1 + "-" + code1 + " با " + classCode2 + "-" + code2);
    }
}

class ExamTimeCollisionError extends Exceptions {
    public ExamTimeCollisionError(String code1, String classCode1, String code2, String classCode2) {
        super("تداخل زمانی امتحان:  " + classCode1 + "-" + code1 + " با " + classCode2 + "-" + code2);
    }
}

class PrerequisitesError extends Exceptions {
    public PrerequisitesError(String prerequisite, String code) {
        super("عدم رعايت پيشنيازی:  " + prerequisite + " --> " + code);
    }
}

class AlreadyPassedError extends Exceptions {
    public AlreadyPassedError(String code) {
        super("عدم امكان اخذ مجدد درس گذرانده شده:  " + code);
    }
}