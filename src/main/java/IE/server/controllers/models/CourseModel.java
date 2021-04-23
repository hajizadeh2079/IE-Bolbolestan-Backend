package IE.server.controllers.models;

public class CourseModel {
    private String id;
    private String code;
    private String classCode;

    public CourseModel(String id, String code, String classCode) {
        this.id = id;
        this.code = code;
        this.classCode = classCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
