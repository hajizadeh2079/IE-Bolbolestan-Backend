<%@ page import="com.example.model.Student" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Student student = (Student) request.getAttribute("student");
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 10%;
            text-align: center;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<ul>
    <li id="std_id">Student Id: <%= student.getId()%></li>
    <li id="first_name">First Name: <%= student.getName()%></li>
    <li id="last_name">Last Name: <%= student.getSecondName()%></li>
    <li id="birthdate">Birthdate: <%= student.getBirthDate()%></li>
    <li id="gpa">GPA: <%= String.valueOf(student.getReportCard().calcGPA()).substring(0, 5)%></li>
    <li id="tpu">Total Passed Units: <%= student.getReportCard().calcTPU()%></li>
</ul>
<table>
    <tr>
        <th>Code</th>
        <th>Grade</th>
    </tr>
    <%for (Map.Entry<String, Long> entry : student.getReportCard().getGrades().entrySet()) {%>
    <tr>
        <td><%= entry.getKey()%></td>
        <td><%= entry.getValue()%></td>
    </tr>
    <%}%>
</table>
</body>
</html>