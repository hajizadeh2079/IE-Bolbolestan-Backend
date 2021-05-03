package IE.server.controllers.models;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportDTO {
    private ArrayList<GradeDTO> gradesHistory;
    private int term;
    private String gpa;

    public ReportDTO(ArrayList<GradeDTO> gradesHistory, int term, double gpa) {
        this.gradesHistory = gradesHistory;
        this.term = term;
        DecimalFormat df = new DecimalFormat("0.00");
        this.gpa = df.format(gpa);
    }

    public ArrayList<GradeDTO> getGradesHistory() {
        return gradesHistory;
    }

    public int getTerm() {
        return term;
    }

    public String getGpa() {
        return gpa;
    }
}
