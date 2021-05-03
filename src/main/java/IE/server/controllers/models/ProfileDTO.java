package IE.server.controllers.models;

public class ProfileDTO {
    private String stdId;
    private String name;
    private String secondName;
    private String birthDate;
    private String field;
    private String faculty;
    private String level;
    private String status;
    private String img;
    private String gpa;
    private String tpu;

    public ProfileDTO(String stdId, String name, String secondName, String birthDate, String field,
                        String faculty, String level, String status, String img, String gpa, String tpu) {
        this.stdId = stdId;
        this.name = name;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.field = field;
        this.faculty = faculty;
        this.level = level;
        this.status = status;
        this.img = img;
        this.gpa = gpa;
        this.tpu = tpu;
    }

    public String getStdId() {
        return stdId;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getField() {
        return field;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getLevel() {
        return level;
    }

    public String getStatus() {
        return status;
    }

    public String getImg() {
        return img;
    }

    public String getGpa() {
        return gpa;
    }

    public String getTpu() {
        return tpu;
    }
}
