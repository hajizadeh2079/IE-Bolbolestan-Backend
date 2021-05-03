package IE.server.repository.models;

public class StudentDAO {
    private String id;
    private String name;
    private String secondName;
    private String email;
    private String password;
    private String birthDate;
    private String field;
    private String faculty;
    private String level;
    private String status;
    private String img;

    public StudentDAO(String id, String name, String secondName, String email, String password, String birthDate, String field, String faculty, String level, String status, String img) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.field = field;
        this.faculty = faculty;
        this.level = level;
        this.status = status;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
}
