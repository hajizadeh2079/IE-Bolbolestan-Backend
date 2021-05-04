package IE.server.controllers.models;

import IE.server.repository.models.CourseDAO;

import java.util.ArrayList;

public class SelectedCourseDTO {
    private ArrayList<CourseDAO> finalizedCourses;
    private ArrayList<CourseDAO> nonFinalizedCourses;
    private ArrayList<CourseDAO> waitingCourses;
    private int sumOfUnits;

    public SelectedCourseDTO(ArrayList<CourseDAO> finalizedCourses, ArrayList<CourseDAO> nonFinalizedCourses, ArrayList<CourseDAO> waitingCourses, int sumOfUnits) {
        this.finalizedCourses = finalizedCourses;
        this.nonFinalizedCourses = nonFinalizedCourses;
        this.waitingCourses = waitingCourses;
        this.sumOfUnits = sumOfUnits;
    }

    public ArrayList<CourseDAO> getFinalizedCourses() {
        return finalizedCourses;
    }

    public ArrayList<CourseDAO> getNonFinalizedCourses() {
        return nonFinalizedCourses;
    }

    public ArrayList<CourseDAO> getWaitingCourses() {
        return waitingCourses;
    }

    public int getSumOfUnits() {
        return sumOfUnits;
    }
}
