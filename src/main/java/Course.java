import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Course {
    private String code;
    private String name;
    private String instructor;
    private long units;
    private JSONObject classTime;
    private ArrayList<String> classTimeDays = new ArrayList<String>();
    private LocalTime classTimeStart;
    private LocalTime classTimeEnd;
    private JSONObject examTime;
    private LocalDateTime examTimeStart;
    private LocalDateTime examTimeEnd;
    private long capacity;
    private JSONArray prerequisites;
    private ArrayList<String> prerequisitesArray = new ArrayList<String>();

    public Course(String _code, String _name, String _instructor, long _unit,
                  JSONObject _classTime, JSONObject _examTime, long _capacity, JSONArray _prerequisites) {
        int i;
        code = _code;
        name = _name;
        instructor = _instructor;
        units = _unit;
        classTime = _classTime;
        JSONArray jsonArray = (JSONArray)_classTime.get("days");
        for (i = 0; i < jsonArray.size(); i++)
            classTimeDays.add((String)jsonArray.get(i));
        String time = (String)_classTime.get("time");
        classTimeStart = LocalTime.parse(time.substring(0, time.indexOf("-")), DateTimeFormatter.ISO_LOCAL_TIME);
        classTimeEnd = LocalTime.parse(time.substring(time.indexOf("-") + 1), DateTimeFormatter.ISO_LOCAL_TIME);
        examTime = _examTime;
        examTimeStart = LocalDateTime.parse((String)_examTime.get("start"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        examTimeEnd = LocalDateTime.parse((String)_examTime.get("end"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
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

    public String getInstructor() {
        return instructor;
    }

    public long getUnits() {
        return units;
    }

    public JSONObject getClassTime() {
        return classTime;
    }

    public ArrayList<String> getClassTimeDays() {
        return classTimeDays;
    }

    public LocalTime getClassTimeStart() {
        return classTimeStart;
    }

    public LocalTime getClassTimeEnd() {
        return classTimeEnd;
    }

    public JSONObject getExamTime() {
        return examTime;
    }

    public LocalDateTime getExamTimeStart() {
        return examTimeStart;
    }

    public LocalDateTime getExamTimeEnd() {
        return examTimeEnd;
    }

    public long getCapacity() {
        return capacity;
    }

    public JSONArray getPrerequisites() {
        return prerequisites;
    }

    public ArrayList<String> getPrerequisitesArray() {
        return prerequisitesArray;
    }
}