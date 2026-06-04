<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Main</title>
</head>
<body>
<h2>Main page</h2>

Hello = <c:out value="${sessionScope.user.name}"/>

<hr/>

<a href="${pageContext.request.contextPath}/controller?command=show_items">
    Show items
</a>

<a href="${pageContext.request.contextPath}/controller?command=show_orders">
    My orders
</a>

<hr/>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="logout">
    <input type="submit" value="logOut">
</form>

</body>
</html>