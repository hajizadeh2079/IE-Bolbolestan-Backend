package IE.server.controllers.models;

import java.util.ArrayList;

public class CourseDTO {
    private String code;
    private String classCode;
    private String name;
    private String instructor;
    private int units;
    private String type;
    private ArrayList<String> classTimeDays;
    private String classTimeStart;
    private String classTimeEnd;
    private String examTimeStart;
    private String examTimeEnd;
    private int capacity;
    private ArrayList<String> prerequisitesNamesArray;
    private int signedUp;

    public CourseDTO(String code, String classCode, String name, String instructor, int units, String type, ArrayList<String> classTimeDays, String classTimeStart, String classTimeEnd, String examTimeStart, String examTimeEnd, int capacity, ArrayList<String> prerequisitesNamesArray, int signedUp) {
        this.code = code;
        this.classCode = classCode;
        this.name = name;
        this.instructor = instructor;
        this.units = units;
        this.type = type;
        this.classTimeDays = classTimeDays;
        this.classTimeStart = classTimeStart;
        this.classTimeEnd = classTimeEnd;
        this.examTimeStart = examTimeStart;
        this.examTimeEnd = examTimeEnd;
        this.capacity = capacity;
        this.prerequisitesNamesArray = prerequisitesNamesArray;
        this.signedUp = signedUp;
    }

    public String getCode() {
        return code;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getName() {
        return name;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getUnits() {
        return units;
    }

    public String getType() {
        return type;
    }

    public ArrayList<String> getClassTimeDays() {
        return classTimeDays;
    }

    public String getClassTimeStart() {
        return classTimeStart;
    }

    public String getClassTimeEnd() {
        return classTimeEnd;
    }

    public String getExamTimeStart() {
        return examTimeStart;
    }

    public String getExamTimeEnd() {
        return examTimeEnd;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<String> getPrerequisitesNamesArray() {
        return prerequisitesNamesArray;
    }

    public int getSignedUp() {
        return signedUp;
    }
}
