package IE.server.controllers.models;

import java.util.ArrayList;

public class ScheduleDTO {
    private ArrayList<CourseDTO> lastFinalizedCourses;
    private int term;

    public ScheduleDTO(ArrayList<CourseDTO> lastFinalizedCourses, int term) {
        this.lastFinalizedCourses = lastFinalizedCourses;
        this.term = term;
    }

    public ArrayList<CourseDTO> getLastFinalizedCourses() {
        return lastFinalizedCourses;
    }

    public int getTerm() {
        return term;
    }
}
