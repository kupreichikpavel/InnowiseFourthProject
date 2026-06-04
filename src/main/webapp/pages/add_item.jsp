<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title>Add item</title>
</head>
<body>
<h2>Add item</h2>

<p style="color:red;">${item_msg}</p>

<form action="${pageContext.request.contextPath}/controller" method="post">
  <input type="hidden" name="command" value="add_item">

  <label>Name:</label>
  <input type="text" name="name" required>

  <br><br>

  <label>Description:</label>
  <input type="text" name="description">

  <br><br>

  <label>Price:</label>
  <input type="number" step="0.01" name="price" required>

  <br><br>

  <button type="submit">Add</button>
</form>

<br>

<a href="${pageContext.request.contextPath}/controller?command=show_items">
  Back to items
</a>

</body>
</html>