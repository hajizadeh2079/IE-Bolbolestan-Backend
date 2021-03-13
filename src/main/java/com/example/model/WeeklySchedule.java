package com.example.model;

import java.util.ArrayList;

public class WeeklySchedule {
    private ArrayList<Course> courses = new ArrayList<Course>();
    private boolean finalized = false;

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public int sumOfUnits() {
        int sum = 0;
        for (Course course: courses)
            sum += course.getUnits();
        return sum;
    }

    public void finalizeSchedule() {
        finalized = true;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public boolean isFinalized() {
        return finalized;
    }
}