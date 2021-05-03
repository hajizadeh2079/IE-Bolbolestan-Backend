package IE.server.repository.models;

public class WeeklyScheduleDAO {
    private String id;
    private String code;
    private String classCode;
    private int status;

    public WeeklyScheduleDAO(String id, String code, String classCode, int status) {
        this.id = id;
        this.code = code;
        this.classCode = classCode;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getClassCode() {
        return classCode;
    }

    public int getStatus() {
        return status;
    }
}
