<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.model.Course" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String std_id = (String) request.getAttribute("std_id");
    ArrayList<Course> filteredCourses = (ArrayList<Course>) request.getAttribute("filtered_courses");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Courses</title>
    <style>
        .course_table {
            width: 100%;
            text-align: center;
        }
        .search_form {
            text-align: center;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<li id="code">Student Id: <%=std_id%></li>
<li id="units">Total Selected Units: 16</li>

<br>

<table>
    <tr>
        <th>Code</th>
        <th>Class Code</th>
        <th>Name</th>
        <th>Units</th>
        <th></th>
    </tr>
    <tr>
        <td>8101001</td>
        <td>01</td>
        <td>Advanced Programming</td>
        <td>3</td>
        <td>
            <form action="" method="POST" >
                <input id="form_action" type="hidden" name="action" value="remove">
                <input id="form_course_code" type="hidden" name="course_code" value="8101001">
                <input id="form_class_code" type="hidden" name="class_code" value="01">
                <button type="submit">Remove</button>
            </form>
        </td>
    </tr>
    <tr>
        <td>8101033</td>
        <td>01</td>
        <td>Islamic Thought 1</td>
        <td>2</td>
        <td>
            <form action="" method="POST" >
                <input id="form_action" type="hidden" name="action" value="remove">
                <input id="form_course_code" type="hidden" name="course_code" value="8101033">
                <input id="form_class_code" type="hidden" name="class_code" value="01">
                <button type="submit">Remove</button>
            </form>
        </td>
    </tr>
</table>

<br>

<form action="" method="POST">
    <button type="submit" name="action" value="submit">Submit Plan</button>
    <button type="submit" name="action" value="reset">Reset</button>
</form>

<br>

<form class="search_form" action="" method="POST">
    <label>Search:</label>
    <input type="text" name="search" value="">
    <button type="submit" name="action" value="search">Search</button>
    <button type="submit" name="action" value="clear">Clear Search</button>
</form>



<br>

<table class="course_table">
    <tr>
        <th>Code</th>
        <th>Class Code</th>
        <th>Name</th>
        <th>Units</th>
        <th>Signed Up</th>
        <th>Capacity</th>
        <th>Type</th>
        <th>Days</th>
        <th>Time</th>
        <th>Exam Start</th>
        <th>Exam End</th>
        <th>Prerequisites</th>
        <th></th>
    </tr>
    <%
        for (Course course: filteredCourses) {
    %>
    <tr>
        <td><%=course.getCode()%></td>
        <td><%=course.getClassCode()%></td>
        <td><%=course.getName()%></td>
        <td><%=course.getUnits()%></td>
        <td><%=course.getSignedUp()%></td>
        <td><%=course.getCapacity()%></td>
        <td><%=course.getType()%></td>
        <%
            StringBuilder temp = new StringBuilder((course.getClassTimeDays().size() == 1) ?
                    course.getClassTimeDays().get(0) :
                    course.getClassTimeDays().get(0) + "|" + course.getClassTimeDays().get(1));
        %>
        <td><%=temp%></td>
        <td><%=course.getClassTimeStart().toString()%>-<%=course.getClassTimeEnd().toString()%></td>
        <td><%=course.getExamTimeStart().toString()%></td>
        <td><%=course.getExamTimeEnd().toString()%></td>
        <%
            temp = new StringBuilder();
            for(int i = 0; i < course.getPrerequisitesArray().size(); i++) {
                if(i != course.getPrerequisitesArray().size() -1 )
                    temp.append(course.getPrerequisitesArray().get(i)).append("|");
                else
                    temp.append(course.getPrerequisitesArray().get(i));
            }
        %>
        <td>temp</td>
        <td>
            <form action="" method="POST" >
                <input id="form_action" type="hidden" name="action" value="add">
                <input id="form_class_code" type="hidden" name="course_code" value=<%=course.getCode()%>>
                <input id="form_class_code" type="hidden" name="class_code" value=<%=course.getClassCode()%>>
                <button type="submit">Add</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>