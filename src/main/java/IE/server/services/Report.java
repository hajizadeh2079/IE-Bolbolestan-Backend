package IE.server.services;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Report {
    private ArrayList<Grade> gradesHistory;
    private Long term;
    private String gpa;

    public Report(ArrayList<Grade> gradesHistory, Long term) {
        this.gradesHistory = gradesHistory;
        this.term = term;
        int n = 0;
        double sum = 0.0;
        for(Grade grade: gradesHistory) {
            if(grade.getStatus() != -1) {
                n += grade.getUnits();
                sum += grade.getGrade() * grade.getUnits();
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        this.gpa = df.format(sum / n);
    }

    public ArrayList<Grade> getGradesHistory() {
        return gradesHistory;
    }

    public Long getTerm() {
        return term;
    }

    public String getGpa() {
        return gpa;
    }
}
