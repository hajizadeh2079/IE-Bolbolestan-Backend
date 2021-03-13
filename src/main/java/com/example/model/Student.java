package com.example.model;

public class Student {
    private String id;
    private String name;
    private String secondName;
    private String birthDate;
    private ReportCard reportCard;
    private WeeklySchedule weeklySchedule;

    public Student(String _id, String _name, String _secondName, String _birthDate) {
        id = _id;
        name = _name;
        secondName = _secondName;
        birthDate = _birthDate;
        reportCard = null;
        weeklySchedule = new WeeklySchedule();
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

    public String getBirthDate() {
        return birthDate;
    }

    public WeeklySchedule getWeeklySchedule() {
        return weeklySchedule;
    }

    public ReportCard getReportCard() {
        return reportCard;
    }

    public void setReportCard(ReportCard reportCard) {
        this.reportCard = reportCard;
    }

    public void addToWeeklySchedule(Course course) {
        weeklySchedule.addCourse(course);
    }

    public void removeFromWeeklySchedule(Course course) {
        weeklySchedule.removeCourse(course);
    }
}