import java.util.ArrayList;

public class Student {
    private String id;
    private String name;
    private String secondName;
    private String birthDate;
    private ReportCard reportCard;
    private ArrayList<Course> finalizedCourses = new ArrayList<Course>();
    private ArrayList<Course> nonFinalizedCourses = new ArrayList<Course>();

    public Student(String _id, String _name, String _secondName, String _birthDate) {
        id = _id;
        name = _name;
        secondName = _secondName;
        birthDate = _birthDate;
        reportCard = null;
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

    public ArrayList<Course> getFinalizedCourses() {
        return finalizedCourses;
    }

    public ArrayList<Course> getNonFinalizedCourses() {
        return nonFinalizedCourses;
    }

    public void setFinalizedCourses(ArrayList<Course> _finalizedCourses) {
        finalizedCourses = _finalizedCourses;
    }

    public void setNonFinalizedCourses(ArrayList<Course> _nonFinalizedCourses) {
        nonFinalizedCourses = _nonFinalizedCourses;
    }

    public ReportCard getReportCard() {
        return reportCard;
    }

    public void setReportCard(ReportCard reportCard) {
        this.reportCard = reportCard;
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