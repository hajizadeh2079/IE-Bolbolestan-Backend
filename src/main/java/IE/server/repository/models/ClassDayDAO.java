package IE.server.repository.models;

public class ClassDayDAO {
    private String code;
    private String classCode;
    private String day;

    public ClassDayDAO(String code, String classCode, String day) {
        this.code = code;
        this.classCode = classCode;
        this.day = day;
    }

    public String getCode() {
        return code;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getDay() {
        return day;
    }
}
