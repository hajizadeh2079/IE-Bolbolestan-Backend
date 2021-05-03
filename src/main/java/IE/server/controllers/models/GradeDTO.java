package IE.server.controllers.models;

public class GradeDTO {
    private String code;
    private String name;
    private int units;
    private int grade;
    private int status;

    public GradeDTO(String code, String name, int units, int grade) {
        this.code = code;
        this.name = name;
        this.units = units;
        if (grade < 10)
            this.status = 0;
        else
            this.status = 1;
        this.grade = grade;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getUnits() {
        return units;
    }

    public int getStatus() {
        return status;
    }

    public int getGrade() {
        return grade;
    }
}