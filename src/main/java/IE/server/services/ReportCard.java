package IE.server.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportCard {
    private ArrayList<Grade> gradesHistory = new ArrayList<Grade>();
    private HashMap<String, Long> grades = new HashMap<>();
    private HashMap<String, Long> codesUnits;

    public ReportCard(JSONArray jsonArray, HashMap<String, Long> _codesUnits) {
        for (Object jsonObject: jsonArray) {
            JSONObject temp = (JSONObject) jsonObject;
            String code = (String) temp.get("code");
            Long grade = (Long) temp.get("grade");
            Long term = (Long) temp.get("term");
            if ((!grades.containsKey(code)) || (grades.get(code) < grade))
                grades.put(code, grade);
            gradesHistory.add(new Grade(code, grade, term));
        }
        codesUnits = _codesUnits;
    }

    public double calcGPA() {
        int n = 0;
        for (String code: grades.keySet())
            n += codesUnits.get(code);
        double sum = 0;
        for (Map.Entry<String, Long> entry : grades.entrySet())
            sum += (codesUnits.get(entry.getKey()) * entry.getValue());
        return sum / n;
    }

    public int calcTPU() {
        int res = 0;
        for (Map.Entry<String, Long> entry : grades.entrySet())
            if (entry.getValue() >= 10)
                res += codesUnits.get(entry.getKey());
        return res;
    }

    public boolean doesPassCourse(String code) {
        return grades.containsKey(code) && grades.get(code) >= 10;
    }

    public HashMap<String, Long> getGrades() {
        return grades;
    }

    public ArrayList<Grade> getGradesHistory() {
        return gradesHistory;
    }
}