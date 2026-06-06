<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Orders</title>
</head>
<body>
<h2>
    <c:choose>
        <c:when test="${sessionScope.user.admin}">
            All orders
        </c:when>
        <c:otherwise>
            My orders
        </c:otherwise>
    </c:choose>
</h2>

<p style="color: green;">
    <c:out value="${order_msg}"/>
</p>

<c:choose>
    <c:when test="${empty orders}">
        <p>No orders yet.</p>
    </c:when>

    <c:otherwise>
        <table border="1">
            <tr>
                <th>ID</th>

                <c:if test="${sessionScope.user.admin}">
                    <th>User ID</th>
                </c:if>

                <th>Item</th>
                <th>Price</th>
                <th>Status</th>
                <th>Created at</th>
                <th>Actions</th>
            </tr>

            <c:forEach var="order" items="${orders}">
                <tr>
                    <td><c:out value="${order.id}"/></td>

                    <c:if test="${sessionScope.user.admin}">
                        <td><c:out value="${order.userId}"/></td>
                    </c:if>

                    <td><c:out value="${order.itemName}"/></td>
                    <td><c:out value="${order.itemPrice}"/></td>
                    <td><c:out value="${order.status}"/></td>
                    <td><c:out value="${order.createdAt}"/></td>

                    <td>
                        <c:if test="${order.status != 'CANCELLED' && order.status != 'COMPLETED'}">
                            <form action="${pageContext.request.contextPath}/controller" method="post" style="display:inline;">
                                <input type="hidden" name="command" value="cancel_order">
                                <input type="hidden" name="order_id" value="${order.id}">
                                <button type="submit">Cancel</button>
                            </form>
                        </c:if>

                        <c:if test="${sessionScope.user.admin && order.status == 'CREATED'}">
                            <form action="${pageContext.request.contextPath}/controller" method="post" style="display:inline;">
                                <input type="hidden" name="command" value="complete_order">
                                <input type="hidden" name="order_id" value="${order.id}">
                                <button type="submit">Complete</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<br>

<a href="${pageContext.request.contextPath}/controller?command=show_items">
    Back to items
</a>

<br><br>

<a href="${pageContext.request.contextPath}/pages/main.jsp">
    Back to main
</a>

</body>
</html>