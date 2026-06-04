<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Items</title>
</head>
<body>
<h2>Items</h2>

<p style="color: green;"><c:out value="${item_msg}"/></p>
<p style="color: green;"><c:out value="${order_msg}"/></p>

<c:if test="${sessionScope.user.admin}">
    <a href="${pageContext.request.contextPath}/controller?command=open_add_item_page">
        Add item
    </a>
    <br><br>
</c:if>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>

        <c:if test="${sessionScope.user.admin}">
            <th>Actions</th>
        </c:if>

        <c:if test="${not sessionScope.user.admin}">
            <th>Order</th>
        </c:if>
    </tr>

    <c:forEach var="item" items="${items}">
        <tr>
            <td><c:out value="${item.id}"/></td>
            <td><c:out value="${item.name}"/></td>
            <td><c:out value="${item.description}"/></td>
            <td><c:out value="${item.price}"/></td>

            <c:if test="${sessionScope.user.admin}">
                <td>
                    <a href="${pageContext.request.contextPath}/controller?command=open_edit_item_page&id=${item.id}">
                        Edit
                    </a>

                    <form action="${pageContext.request.contextPath}/controller" method="post" style="display:inline;">
                        <input type="hidden" name="command" value="delete_item">
                        <input type="hidden" name="id" value="${item.id}">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </c:if>

            <c:if test="${not sessionScope.user.admin}">
                <td>
                    <form action="${pageContext.request.contextPath}/controller" method="post">
                        <input type="hidden" name="command" value="create_order">
                        <input type="hidden" name="item_id" value="${item.id}">
                        <button type="submit">Order</button>
                    </form>
                </td>
            </c:if>
        </tr>
    </c:forEach>
</table>

<br>

<a href="${pageContext.request.contextPath}/controller?command=show_orders">
    My orders
</a>

<br><br>

<a href="${pageContext.request.contextPath}/pages/main.jsp">
    Back to main
</a>

</body>
</html>