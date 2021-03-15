<%@ page import="com.example.model.UnitSelectionSystem" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
</head>
<%
    String std_id = UnitSelectionSystem.getInstance().getLoggedInStudent();
%>
<body>
<ul>
    <li id="std_id">Student Id: <%= std_id%> </li>
    <li>
        <a href="courses">Select Courses</a>
    </li>
    <li>
        <a href="plan">Submitted plan</a>
    </li>
    <li>
        <a href="profile">Profile</a>
    </li>
    <li>
        <a href="logout">Log Out</a>
    </li>
</ul>
</body>
</html>