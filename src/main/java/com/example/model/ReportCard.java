package com.example.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ReportCard {
    private HashMap<String, Long> grades = new HashMap<>();
    private HashMap<String, Long> codesUnits;

    public ReportCard(JSONArray jsonArray, HashMap<String, Long> _codesUnits) {
        JSONObject temp;
        for (Object jsonObject: jsonArray) {
            temp = (JSONObject) jsonObject;
            grades.put((String) temp.get("code"), (Long) temp.get("grade"));
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
}