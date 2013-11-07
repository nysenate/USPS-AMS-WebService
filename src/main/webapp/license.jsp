<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="gov.nysenate.ams.util.Application" %>
<html>
<head>
    <title>AMS License Info</title>
</head>
<body>
    <h3>AMS License Info</h3>
    <p>Api Version: <%= Application.getAmsNativeProvider().getApiVersion() %></p>
    <p>Days Until Data Expires: <%= Application.getAmsNativeProvider().getDataExpireDays() %></p>
    <p>Days Until Library Expires: <%= Application.getAmsNativeProvider().getLibraryExpireDays() %></p>
</body>
</html>