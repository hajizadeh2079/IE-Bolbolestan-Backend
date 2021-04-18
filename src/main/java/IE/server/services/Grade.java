package IE.server.services;

public class Grade {
    private String code;
    private Long grade;
    private Long term;

    public Grade(String _code, Long _grade, Long _term) {
        code = _code;
        grade = _grade;
        term = _term;
    }

    public String getCode() {
        return code;
    }

    public Long getGrade() {
        return grade;
    }

    public Long getTerm() {
        return term;
    }
}