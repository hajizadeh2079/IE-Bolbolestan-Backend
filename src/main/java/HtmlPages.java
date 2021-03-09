import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Map;

public class HtmlPages {

    public String submitOkPage(Object data) {
        return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Submit OK</title>" +
                "</head><body>Your request submitted successfully</body></html>";
    }

    public String submitFailedPage(Object data) {
        return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Submit Failed</title>" +
                "</head><body>Your request failed</body></html>";
    }

    public String submitPage(Object data) {
        Student student = (Student) data;
        String html = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Submit</title>" +
                "<style> li {padding: 5px}</style></head><body><ul><li id=\"code\">Student Id: </li>" +
                "<li id=\"units\">Total Units: </li><form action=\"\" method=\"POST\" >" +
                "<button type=\"submit\">submit</button></form></ul></body></html>";
        Document doc = Jsoup.parse(html);
        doc.getElementById("code").append(student.getId());
        doc.getElementById("units").append(String.valueOf(student.getWeeklySchedule().sumOfUnits()));
        html = doc.toString();
        return html;
    }

    public String changePlanPage(Object data) {
        Student student = (Student) data;
        String html = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\">" +
                "<title>Change Plan</title><style>table{text-align: center;}</style></head><body><table>" +
                "<tr><th>Code</th><th>Class Code</th><th>Name</th><th>Units</th><th></th></tr></table></body></html>";
        Document doc = Jsoup.parse(html);
        Element table = doc.select("table").first();
        for (Course course: student.getWeeklySchedule().getCourses()) {
            table.append("<tr></tr>");
            Element tableRow = doc.select("tr").last();
            tableRow.append("<td>" + course.getCode() + "</td>");
            tableRow.append("<td>" + course.getClassCode() + "</td>");
            tableRow.append("<td>" + course.getName() + "</td>");
            tableRow.append("<td>" + course.getUnits() + "</td>");
            String form = "<form action=\"\" method=\"POST\" ><input id=\"form_course_code\" type=\"hidden\"" +
                    "name=\"course_code\"value=" + course.getCode() + "><input id=\"form_class_code\"" +
                    "type=\"hidden\"name=\"class_code\" value=" + course.getClassCode() +
                    "><button type=\"submit\">Remove</button></form>";
            tableRow.append("<td>" + form + "</td>");
        }
        html = doc.toString();
        return html;
    }

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
        Student student = (Student) data;
        String html = "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Plan</title>" +
              "<style> table{width: 100%; text-align: center;} table, th, td{border: 1px solid black;border-collapse: collapse;" +
               "</style></head><body><table><tr><th></th><th>07:30-09:00</th><th>09:00-10:30</th><th>10:30-12:00</th>" +
                "<th>14:00-15:30</th><th>16:00-17:30</th></tr>" +
                "<tr id=\"Saturday\"><td>Saturday</td><td id=\"07:30\"></td><td id=\"09:00\"></td><td id=\"10:30\"></td><td id=\"14:00\"></td><td id=\"16:00\"></td></tr>" +
                "<tr id=\"Sunday\"><td>Sunday</td><td id=\"07:30\"></td><td id=\"09:00\"></td><td id=\"10:30\"></td><td id=\"14:00\"></td><td id=\"16:00\"></td></tr>" +
                "<tr id=\"Monday\"><td>Monday</td><td id=\"07:30\"></td><td id=\"09:00\"></td><td id=\"10:30\"></td><td id=\"14:00\"></td><td id=\"16:00\"></td></tr>" +
                "<tr id=\"Tuesday\"><td>Tuesday</td><td id=\"07:30\"></td><td id=\"09:00\"></td><td id=\"10:30\"></td><td id=\"14:00\"></td><td id=\"16:00\"></td></tr>" +
                "<tr id=\"Wednesday\"><td>Wednesday</td><td id=\"07:30\"></td><td id=\"09:00\"></td><td id=\"10:30\"></td><td id=\"14:00\"></td><td id=\"16:00\"></td></tr>" +
                "</table></body></html>";
        Document doc = Jsoup.parse(html);
        for(Course course: student.getWeeklySchedule().getCourses()) {
            for (String day: course.getClassTimeDays()) {
                Element tableRow = doc.getElementById(day);
                Element td = tableRow.getElementById(course.getClassTimeStart().toString());
                td.append(course.getName());
            }
        }
        html = doc.toString();
        return html;
    }

    public String profilePage(Object data) {
        Student student = (Student) data;
        String html = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Profile</title>" +
                "<style> li {padding: 5px} table{width: 10%; text-align: center;} </style>" +
                "</head><body><ul> <li id=\"std_id\">Student Id: </li> <li id=\"first_name\">First Name: </li>" +
                "<li id=\"last_name\">Last Name: </li> <li id=\"birthdate\">Birthdate: </li>" +
                "<li id=\"gpa\">GPA: </li><li id=\"tpu\">Total Passed Units: </li></ul>" +
                "<table><tr><th>Code</th><th>Grade</th></tr></table></body></html>";
        Document doc = Jsoup.parse(html);
        doc.getElementById("std_id").append(student.getId());
        doc.getElementById("first_name").append(student.getName());
        doc.getElementById("last_name").append(student.getSecondName());
        doc.getElementById("birthdate").append(student.getBirthDate());
        ReportCard reportCard = student.getReportCard();
        String gpa = String.valueOf(reportCard.calcGPA()).substring(0, 5);
        doc.getElementById("gpa").append(gpa);
        String tpu = String.valueOf(reportCard.calcTPU());
        doc.getElementById("tpu").append(tpu);
        Element table = doc.select("table").first();
        for (Map.Entry<String, Long> entry : reportCard.getGrades().entrySet()) {
            table.append("<tr></tr>");
            Element tableRow = doc.select("tr").last();
            tableRow.append("<td>" + entry.getKey() + "</td>");
            tableRow.append("<td>" + entry.getValue() + "</td>");
        }
        html = doc.toString();
        return html;
    }

    public String NotFoundPage(Object data) {
        return "<html lang=\"en\"><head><meta charset=\"UTF-8\"><title>404 Error</title></head>" +
                "<body><h1>404<br>Page Not Found</h1></body></html>";
    }
}