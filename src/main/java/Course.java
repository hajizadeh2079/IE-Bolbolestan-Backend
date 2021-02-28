import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Course {
    private String code;
    private String name;
    private String instructor;
    private int units;
    private JSONObject classTime;
    private ArrayList<String> classTimeDays = new ArrayList<String>();
    private LocalTime classTimeTime;
    private JSONObject examTime;
    private LocalDateTime examTimeStart;
    private LocalDateTime examTimeEnd;
    private int capacity;
    private JSONArray prerequisites;
    private ArrayList<String> prerequisitesArray = new ArrayList<String>();

    public Course(String _code, String _name, String _instructor, int _unit,
                  JSONObject _classTime, JSONObject _examTime, int _capacity, JSONArray _prerequisites) {
        int i;
        code = _code;
        name = _name;
        instructor = _instructor;
        units = _unit;
        classTime = _classTime;
        JSONArray jsonArray = (JSONArray)_classTime.get("days");
        for (i = 0; i < jsonArray.size(); i++)
            classTimeDays.add((String)jsonArray.get(i));
        classTimeTime = LocalTime.parse((String)_classTime.get("time"));
        examTime = _examTime;
        examTimeStart = LocalDateTime.parse((String)_examTime.get("start"));
        examTimeEnd = LocalDateTime.parse((String)_examTime.get("end"));
        capacity = _capacity;
        prerequisites = _prerequisites;
        for (i = 0; i < _prerequisites.size(); i++)
            prerequisitesArray.add((String)_prerequisites.get(i));
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}