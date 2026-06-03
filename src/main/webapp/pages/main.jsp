<%--
  Created by IntelliJ IDEA.
  User: alexey
  Date: 27.05.2026
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>
</head>
<body>
Hello = ${user}
<br/>
Hi (redirect/forward) = ${user}
<hr/>
${filter_attr}
<hr/>
<form action="controller">
    <input type="hidden" name="command" value="logout">
    <input type="submit" value="logOut">
</form>
</body>
</html>
