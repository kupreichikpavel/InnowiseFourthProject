<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Sign in</title>
</head>
<body>
<h2>Sign in</h2>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="login">

    <label>Login:</label>
    <input type="text" name="login" required>

    <br><br>

    <label>Password:</label>
    <input type="password" name="pass" required>

    <br><br>

    <button type="submit">Sign in</button>
</form>

<c:if test="${not empty sessionScope.register_msg}">
    <p style="color: green;">
        <c:out value="${sessionScope.register_msg}"/>
    </p>
    <c:remove var="register_msg" scope="session"/>
</c:if>

<p style="color:red;"><c:out value="${login_msg}"/></p>
<hr>

<a href="${pageContext.request.contextPath}/pages/register.jsp">Create account</a>

</body>
</html>