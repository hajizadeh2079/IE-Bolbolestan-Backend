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
        super(MinOrMax + "UnitsError");
    }
}

class CapacityError extends Exceptions {
    public CapacityError(String code) {
        super("CapacityError " + code);
    }
}

class ClassTimeCollisionError extends Exceptions {
    public ClassTimeCollisionError(String code1, String classCode1, String code2, String classCode2) {
        super("ClassTimeCollisionError " + code1 + "-" + classCode1 + " " + code2 + "-" + classCode2);
    }
}

class ExamTimeCollisionError extends Exceptions {
    public ExamTimeCollisionError(String code1, String classCode1, String code2, String classCode2) {
        super("ExamTimeCollisionError " + code1 + "-" + classCode1 + " " + code2 + "-" + classCode2);
    }
}

class PrerequisitesError extends Exceptions {

    public PrerequisitesError(String code) {
        super("prerequisites error for course : " + code);
    }
}