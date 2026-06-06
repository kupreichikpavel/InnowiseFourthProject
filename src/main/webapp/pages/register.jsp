<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Registration</title>
</head>
<body>
<h2>Registration</h2>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="add_user">

    <label>Login:</label>
    <input type="text" name="login" required>

    <br><br>

    <label>Name:</label>
    <input type="text" name="name" required>

    <br><br>

    <label>Password:</label>
    <input type="password" name="password" required>

    <br><br>

    <button type="submit">Sign up</button>
</form>

<p style="color:green;"><c:out value="${register_msg}"/></p>

<hr>

<a href="${pageContext.request.contextPath}/index.jsp">Back to sign in</a>

</body>
</html>