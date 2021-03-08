import org.json.simple.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class HtmlPages {

    public String coursesPage(Object data) {
        ArrayList<Course> courses = (ArrayList<Course>) data;
        String html = "<html lang=\"en\"><head><title>Courses</title>" +
                "</head><style>table{ width: 100%; text-align: center;}</style></head>"
                + "<body>" + "<table><tr><th>Code</th><th>Class Code</th><th>Name</th>" +
                "<th>Units</th><th>Capacity</th><th>Type</th><th>Days</th><th>Time</th>" +
                "<th>Exam Start</th><th>Exam End</th><th>Prerequisites</th><th>Links</th></tr></table></body></html>";
        Document doc = Jsoup.parse(html);
        Element table = doc.select("table").first();
        for (Course course: courses) {
            table.append("<tr></tr>");
            Element tableRow = doc.select("tr").last();
            tableRow.append("<td>" + course.getCode() + "</td>");
            tableRow.append("<td>" + course.getClassCode() + "</td>");
            tableRow.append("<td>" + course.getName() + "</td>");
            tableRow.append("<td>" + course.getUnits() + "</td>");
            tableRow.append("<td>" + course.getCapacity() + "</td>");
            tableRow.append("<td>" + course.getType() + "</td>");
            StringBuilder temp = new StringBuilder((course.getClassTimeDays().size() == 1) ? course.getClassTimeDays().get(0) :
                    course.getClassTimeDays().get(0) + "|" + course.getClassTimeDays().get(1));
            tableRow.append("<td>" + temp + "</td>");
            tableRow.append("<td>" + course.getClassTimeStart().toString() + "-" +  course.getClassTimeEnd().toString() + "</td>");
            tableRow.append("<td>" + course.getExamTimeStart().toString() + "</td>");
            tableRow.append("<td>" + course.getExamTimeEnd() + "</td>");
            temp = new StringBuilder();
            for(int i = 0; i < course.getPrerequisitesArray().size(); i++) {
                if(i != course.getPrerequisitesArray().size() -1 )
                    temp.append(course.getPrerequisitesArray().get(i)).append("|");
                else
                    temp.append(course.getPrerequisitesArray().get(i));
            }
            tableRow.append("<td>" + temp + "</td>");
            String link = "<a href=\"/course/" + course.getCode() + "/" + course.getClassCode() + "\">Link</a>";
            tableRow.append("<td>" + link + "</td>");
        }
        html = doc.toString();
        return html;
    }

    public String coursePage(Object data) {
        Course course = (Course) data;
        String html = "<html lang=\"en\"><head><title>Course</title>" +
                "</head><style>li{ padding: 5px;}</style></head>" +
                "<body><ul><li id=\"code\">Code: </li><li id=\"class_code\">Class Code: </li>" +
                "<li id=\"units\">Units: </li><li id=\"days\">Days: </li><li id=\"time\">Time: </li>" +
                "<form action=\"\" method=\"POST\" ><label>Student ID:</label>" +
                "<input type=\"text\" name=\"std_id\" value=\"\"/><button type=\"submit\">Add</button>" +
                "</form></ul></body></html>";
        Document doc = Jsoup.parse(html);
        doc.getElementById("code").append(course.getCode());
        doc.getElementById("class_code").append(course.getClassCode());
        doc.getElementById("units").append(Long.toString(course.getUnits()));
        String temp = ((course.getClassTimeDays().size() == 1) ? course.getClassTimeDays().get(0) :
                course.getClassTimeDays().get(0) + ", " + course.getClassTimeDays().get(1));
        doc.getElementById("days").append(temp);
        doc.getElementById("time").append(course.getClassTimeStart().toString() + "-" +  course.getClassTimeEnd().toString());
        html = doc.toString();
        return html;
    }

    public String planPage(Object data) {
        String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Plan</title>" +
              "<style> table{width: 100%; text-align: center;} table, th, td{border: 1px solid black;border-collapse: collapse;" +
               "</style></head><body><table><tr><th></th><th>07:30-09:00</th><th>09:00-10:30</th><th>10:30-12:00</th>" +
                "<th>14:00-15:30</th><th>16:00-17:30</th></tr><tr><td>Saturday</td><td></td><td></td><td></td><td></td><td></td></tr>" +
                "<tr><td>Sunday</td><td></td><td></td><td></td><td></td><td></td></tr><tr><td>Monday</td><td></td><td></td><td></td><td></td><td></td></tr>" +
                "<tr><td>Tuesday</td><td></td><td></td><td></td><td></td><td></td></tr><tr><td>Wednesday</td><td></td><td></td><td></td><td></td><td></td></tr>";
        return html;
    }
}