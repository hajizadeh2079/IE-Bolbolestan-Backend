package IE.server.repository.models;

public class GradeDAO {
    private String id;
    private String code;
    private int term;
    private int grade;

    public GradeDAO(String id, String code, int term, int grade) {
        this.id = id;
        this.code = code;
        this.term = term;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getTerm() {
        return term;
    }

    public int getGrade() {
        return grade;
    }
}
