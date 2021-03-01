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