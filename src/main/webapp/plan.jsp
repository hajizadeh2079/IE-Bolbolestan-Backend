<%@ page import="com.example.model.Course" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.Course" %>
<%@ page import="java.util.HashMap" %>
<%
    String std_id = (String) request.getAttribute("std_id");
    HashMap<String, String> plan = (HashMap<String, String>) request.getAttribute("plan");
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Plan</title>
    <style>
        table{
            width: 100%;
            text-align: center;

        }
        table, th, td{
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<li id="code">Student Id: <%= std_id%></li>
<br>
<table>
    <tr>
        <th></th>
        <th>7:30-9:00</th>
        <th>9:00-10:30</th>
        <th>10:30-12:00</th>
        <th>14:00-15:30</th>
        <th>16:00-17:30</th>
    </tr>
    <tr>
        <td>Saturday</td>
        <td><%= plan.getOrDefault("Saturday-07:30", "")%></td>
        <td><%= plan.getOrDefault("Saturday-09:00", "")%></td>
        <td><%= plan.getOrDefault("Saturday-10:30", "")%></td>
        <td><%= plan.getOrDefault("Saturday-14:00", "")%></td>
        <td><%= plan.getOrDefault("Saturday-16:00", "")%></td>
    </tr>
    <tr>
        <td>Sunday</td>
        <td><%= plan.getOrDefault("Sunday-07:30", "")%></td>
        <td><%= plan.getOrDefault("Sunday-09:00", "")%></td>
        <td><%= plan.getOrDefault("Sunday-10:30", "")%></td>
        <td><%= plan.getOrDefault("Sunday-14:00", "")%></td>
        <td><%= plan.getOrDefault("Sunday-16:00", "")%></td>
    </tr>
    <tr>
        <td>Monday</td>
        <td><%= plan.getOrDefault("Monday-07:30", "")%></td>
        <td><%= plan.getOrDefault("Monday-09:00", "")%></td>
        <td><%= plan.getOrDefault("Monday-10:30", "")%></td>
        <td><%= plan.getOrDefault("Monday-14:00", "")%></td>
        <td><%= plan.getOrDefault("Monday-16:00", "")%></td>
    </tr>
    <tr>
        <td>Tuesday</td>
        <td><%= plan.getOrDefault("Tuesday-07:30", "")%></td>
        <td><%= plan.getOrDefault("Tuesday-09:00", "")%></td>
        <td><%= plan.getOrDefault("Tuesday-10:30", "")%></td>
        <td><%= plan.getOrDefault("Tuesday-14:00", "")%></td>
        <td><%= plan.getOrDefault("Tuesday-16:00", "")%></td>
    </tr>
    <tr>
        <td>Wednesday</td>
        <td><%= plan.getOrDefault("Wednesday-07:30", "")%></td>
        <td><%= plan.getOrDefault("Wednesday-09:00", "")%></td>
        <td><%= plan.getOrDefault("Wednesday-10:30", "")%></td>
        <td><%= plan.getOrDefault("Wednesday-14:00", "")%></td>
        <td><%= plan.getOrDefault("Wednesday-16:00", "")%></td>
    </tr>
</table>
</body>
</html>