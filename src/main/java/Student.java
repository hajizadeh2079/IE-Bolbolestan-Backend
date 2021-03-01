import java.util.ArrayList;

public class Student {
    private String id;
    private String name;
    private String enteredAt;
    private ArrayList<Course> finalizedCourses = new ArrayList<Course>();
    private ArrayList<Course> nonFinalizedCourses = new ArrayList<Course>();

    public Student(String _id, String _name, String _enteredAt) {
        id = _id;
        name = _name;
        enteredAt = _enteredAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEnteredAt() {
        return enteredAt;
    }

    public ArrayList<Course> getFinalizedCourses() {
        return finalizedCourses;
    }

    public ArrayList<Course> getNonFinalizedCourses() {
        return nonFinalizedCourses;
    }

    public void setFinalizedCourses(ArrayList<Course> finalizedCourses) {
        this.finalizedCourses = finalizedCourses;
    }

    public void setNonFinalizedCourses(ArrayList<Course> nonFinalizedCourses) {
        this.nonFinalizedCourses = nonFinalizedCourses;
    }

    public String isCourseExist(String code) throws OfferingNotFound {
        for (Course course: finalizedCourses)
            if (course.getCode().equals(code))
                return "finalized";
        for (Course course: nonFinalizedCourses)
            if (course.getCode().equals(code))
                return "notFinalized";
        throw new OfferingNotFound();
    }

    public int studentUnitsCount() {
        int units = 0;
        for (Course course: finalizedCourses)
            units += course.getUnits();
        for (Course course: nonFinalizedCourses)
            units += course.getUnits();
        return units;
    }
}