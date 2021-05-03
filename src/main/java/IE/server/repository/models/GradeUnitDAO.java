package IE.server.repository.models;

public class GradeUnitDAO {
    private int grade;
    private int units;

    public GradeUnitDAO(int grade, int units) {
        this.grade = grade;
        this.units = units;
    }

    public int getGrade() {
        return grade;
    }

    public int getUnits() {
        return units;
    }
}
