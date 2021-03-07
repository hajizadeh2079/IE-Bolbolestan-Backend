public class HtmlPages {

    public String coursesPage(Object data) {
        String html = "<html lang=\"en\"><head><title>Courses</title>" +
                "</head><style>table{ width: 100%; text-align: center;}</style></head>"
                + "<body>"
                + "<table><tr><th>Code</th><th>Class Code</th><th>Name</th>" +
                "<th>Units</th><th>Capacity</th><th>Type</th><th>Days</th><th>Time</th>" +
                "<th>Exam Start</th><th>Exam End</th><th>Prerequisites</th><th>Links</th></tr></table>"
                +"</body></html>";
        return html;
    }
}