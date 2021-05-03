package IE.server.repository.models;

public class PrerequisiteDAO {
    private String code;
    private String classCode;
    private String prerequisite;

    public PrerequisiteDAO(String code, String classCode, String prerequisite) {
        this.code = code;
        this.classCode = classCode;
        this.prerequisite = prerequisite;
    }

    public String getCode() {
        return code;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getPrerequisite() {
        return prerequisite;
    }
}
