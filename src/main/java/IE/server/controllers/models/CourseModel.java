package IE.server.controllers.models;

public class CourseModel {
    private String code;
    private String classCode;

    public CourseModel(String code, String classCode) {
        this.code = code;
        this.classCode = classCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
