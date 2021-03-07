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
            tableRow.append("<td>" + "<a href=\"/course/8101001/01\">Link</a>" + "</td>");
        }
        html = doc.toString();
        return html;
    }
}