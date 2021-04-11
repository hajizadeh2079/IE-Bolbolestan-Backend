package com.example.model;

public class Student {
    private String id;
    private String name;
    private String secondName;
    private String birthDate;
    private String field;
    private String faculty;
    private String level;
    private String status;
    private String img;
    private ReportCard reportCard;
    private WeeklySchedule lastFinalized;
    private WeeklySchedule weeklySchedule;

    public Student(String _id, String _name, String _secondName, String _birthDate, String _field, String _faculty,
                   String _level, String _status, String _img) {
        id = _id;
        name = _name;
        secondName = _secondName;
        birthDate = _birthDate;
        field = _field;
        faculty = _faculty;
        level = _level;
        status = _status;
        img = _img;
        reportCard = null;
        lastFinalized = new WeeklySchedule();
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

    public WeeklySchedule getWeeklySchedule() {
        return weeklySchedule;
    }

    public ReportCard getReportCard() {
        return reportCard;
    }

    public WeeklySchedule getLastFinalized() {
        return lastFinalized;
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

    public void resetPlan() {
        weeklySchedule = new WeeklySchedule();
        for (Course course: lastFinalized.getCourses())
            weeklySchedule.addCourse(course);
    }

    public void submitPlan() {
        for (Course course: weeklySchedule.getCourses())
            course.decreasingRemainingCapacity();
        for (Course course: lastFinalized.getCourses())
            course.increasingRemainingCapacity();
        lastFinalized = new WeeklySchedule();
        for (Course course: weeklySchedule.getCourses())
            lastFinalized.addCourse(course);
    }
}