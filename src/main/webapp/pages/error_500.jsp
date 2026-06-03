<%--
  Created by IntelliJ IDEA.
  User: alexey
  Date: 27.05.2026
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>500</title>
</head>
<body>
Request from : ${pageContext.errorData.requestURI} is failed <br/>
        Servlet name: ${pageContext.errorData.servletName} <br/>
        Status code: ${pageContext.errorData.statusCode} <br/>
        Exception : ${pageContext.exception} <br/>
        <br/><br/><br/>
        Message from excepfion: ${error_msg}
</body>
</html>
