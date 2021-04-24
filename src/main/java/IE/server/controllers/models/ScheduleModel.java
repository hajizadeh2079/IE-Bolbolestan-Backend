package IE.server.controllers.models;

import IE.server.services.Course;

import java.util.ArrayList;

public class ScheduleModel {
    private ArrayList<Course> lastFinalizedCourses = new ArrayList<Course>();
    private Long term;

    public ScheduleModel(ArrayList<Course> lastFinalizedCourses, Long term) {
        this.lastFinalizedCourses = lastFinalizedCourses;
        this.term = term;
    }

    public ArrayList<Course> getLastFinalizedCourses() {
        return lastFinalizedCourses;
    }

    public Long getTerm() {
        return term;
    }
}
