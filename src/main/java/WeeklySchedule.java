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