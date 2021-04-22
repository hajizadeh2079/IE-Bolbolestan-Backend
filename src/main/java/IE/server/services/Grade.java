package IE.server.services;

public class Grade {
    private String code;
    private String name;
    private Long units;
    private Long status;
    private Long grade;
    private Long term;

    public Grade(String code, String name, Long units, Long grade, Long term) {
        this.code = code;
        this.name = name;
        this.units = units;
        if (grade < 10)
            this.status = 0L;
        else
            this.status = 1L;
        this.grade = grade;
        this.term = term;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Long getUnits() {
        return units;
    }

    public Long getStatus() {
        return status;
    }

    public Long getGrade() {
        return grade;
    }

    public Long getTerm() {
        return term;
    }
}