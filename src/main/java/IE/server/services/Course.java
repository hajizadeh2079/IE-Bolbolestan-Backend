package com.example.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Course {
    private String code;
    private String classCode;
    private String name;
    private String instructor;
    private long units;
    private String type;
    private ArrayList<String> classTimeDays = new ArrayList<String>();
    private LocalTime classTimeStart;
    private LocalTime classTimeEnd;
    private LocalDateTime examTimeStart;
    private LocalDateTime examTimeEnd;
    private long capacity;
    private ArrayList<String> prerequisitesArray = new ArrayList<String>();
    private long signedUp;

    public Course(String _code, String _classCode, String _name, String _instructor, String _type, long _unit,
                  JSONObject _classTime, JSONObject _examTime, long _capacity, JSONArray _prerequisites) {
        int i;
        code = _code;
        classCode = _classCode;
        name = _name;
        instructor = _instructor;
        type = _type;
        units = _unit;
        JSONArray jsonArray = (JSONArray)_classTime.get("days");
        for (i = 0; i < jsonArray.size(); i++)
            classTimeDays.add((String)jsonArray.get(i));
        String time = (String)_classTime.get("time");
        String temp = time.substring(0, time.indexOf("-"));
        temp = (temp.length() < 5) ? "0" + temp : temp;
        classTimeStart = LocalTime.parse(temp, DateTimeFormatter.ISO_LOCAL_TIME);
        temp = time.substring(time.indexOf("-") + 1);
        temp = (temp.length() < 5) ? "0" + temp : temp;
        classTimeEnd = LocalTime.parse(temp, DateTimeFormatter.ISO_LOCAL_TIME);
        examTimeStart = LocalDateTime.parse((String)_examTime.get("start"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        examTimeEnd = LocalDateTime.parse((String)_examTime.get("end"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        capacity = _capacity;
        signedUp =  0;
        for (i = 0; i < _prerequisites.size(); i++)
            prerequisitesArray.add((String)_prerequisites.get(i));
    }

    public long getSignedUp() {
        return signedUp;
    }

    public void decreaseSignedUp() {
        signedUp--;
    }

    public void increaseSignedUp() {
        signedUp++;
    }

    public void decreaseCapacity() {
        capacity--;
    }

    public void increaseCapacity() {
        capacity++;
    }

    public long getRemainingCapacity() {
        return capacity - signedUp;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getInstructor() {
        return instructor;
    }

    public long getUnits() {
        return units;
    }

    public ArrayList<String> getClassTimeDays() {
        return classTimeDays;
    }

    public LocalTime getClassTimeStart() {
        return classTimeStart;
    }

    public LocalTime getClassTimeEnd() {
        return classTimeEnd;
    }

    public LocalDateTime getExamTimeStart() {
        return examTimeStart;
    }

    public LocalDateTime getExamTimeEnd() {
        return examTimeEnd;
    }

    public long getCapacity() {
        return capacity;
    }

    public String getClassCode() {
        return classCode;
    }

    public ArrayList<String> getPrerequisitesArray() {
        return prerequisitesArray;
    }

    public String getType() {
        return type;
    }
}