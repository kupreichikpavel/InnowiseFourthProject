<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.innowisefourthproject.entity.Item" %>

<%
  Item item = (Item) request.getAttribute("item");
%>

<html>
<head>
  <title>Edit item</title>
</head>
<body>
<h2>Edit item</h2>

<p style="color:red;">${item_msg}</p>

<% if (item != null) { %>
<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="update_item">
  <input type="hidden" name="id" value="<%= item.getId() %>">

  <label>Name:</label>
  <input type="text" name="name" value="<%= item.getName() %>" required>

  <br><br>

  <label>Description:</label>
  <input type="text" name="description" value="<%= item.getDescription() %>">

  <br><br>

  <label>Price:</label>
  <input type="number" step="0.01" name="price" value="<%= item.getPrice() %>" required>

  <br><br>

  <button type="submit">Update</button>
</form>
<% } else { %>
<p>Item not found</p>
<% } %>

<br>

<a href="${pageContext.request.contextPath}/controller?command=show_items">
  Back to items
</a>

</body>
</html>