package IE.server.controllers.models;

import IE.server.services.Course;

import java.util.ArrayList;

public class SelectedCourseModel {
    private ArrayList<Course> finalizedCourses;
    private ArrayList<Course> nonFinalizedCourses;
    private ArrayList<Course> waitingCourses;
    private int sumOfUnits;

    public SelectedCourseModel(ArrayList<Course> finalizedCourses, ArrayList<Course> nonFinalizedCourses, ArrayList<Course> waitingCourses, int sumOfUnits) {
        this.finalizedCourses = finalizedCourses;
        this.nonFinalizedCourses = nonFinalizedCourses;
        this.waitingCourses = waitingCourses;
        this.sumOfUnits = sumOfUnits;
    }

    public ArrayList<Course> getFinalizedCourses() {
        return finalizedCourses;
    }

    public ArrayList<Course> getNonFinalizedCourses() {
        return nonFinalizedCourses;
    }

    public ArrayList<Course> getWaitingCourses() {
        return waitingCourses;
    }

    public int getSumOfUnits() {
        return sumOfUnits;
    }
}
