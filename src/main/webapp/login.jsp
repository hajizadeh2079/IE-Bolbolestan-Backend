<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form class="search_form" action="/" method="POST">
    <label>Student ID:</label>
    <input type="text" name="std_id" value="">
    <button type="submit">Login</button>
    <%if(request.getAttribute("bad_id") != null && request.getAttribute("bad_id").equals("true")) {%>
    <h3>Please Enter a valid Student ID.</h3>
    <%}%>
</form>
</body>
</html>