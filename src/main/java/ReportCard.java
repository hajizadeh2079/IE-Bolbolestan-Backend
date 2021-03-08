import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class ReportCard {
    private HashMap<String, Float> grades = new HashMap<String, Float>();

    public ReportCard(JSONArray jsonArray) {
        JSONObject temp;
        for (Object jsonObject: jsonArray) {
            temp = (JSONObject) jsonObject;
            grades.put((String) temp.get("code"), (Float) temp.get("grade"));
        }
    }
}