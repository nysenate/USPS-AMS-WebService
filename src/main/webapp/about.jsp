<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="gov.nysenate.ams.util.Application" %>
<% Application app = (Application) application.getAttribute(Application.CONTEXT_ATTRIBUTE); %>
<html>
<head>
    <title>AMS License Info</title>
</head>
<body>
    <h3>AMS License Info</h3>
    <hr/>
    <p>Api Version: <%= app.getAmsNativeProvider().getApiVersion() %></p>
    <p>Days Until Data Expires: <%= app.getAmsNativeProvider().getDataExpireDays() %></p>
    <br/>
    <h3>Environment Info</h3>
    <hr/>
    <p><strong>Java Library Path</strong>: <%= System.getProperty("java.library.path") %></p>
    <p><strong>AMS System Path</strong>: <%= app.getConfig().getString("ams.cfg.system.path") %></p>
    <p><strong>Address 1 Path</strong>: <%= app.getConfig().getString("ams.cfg.address1.path") %></p>
    <p><strong>Address Index Path</strong>: <%= app.getConfig().getString("ams.cfg.addrIndex.path") %></p>
    <p><strong>City/State Path</strong>: <%= app.getConfig().getString("ams.cfg.cityState.path") %></p>
    <p><strong>Cross Ref Path</strong>: <%= app.getConfig().getString("ams.cfg.crossRef.path") %></p>
    <p><strong>ELOT Path</strong>: <%= app.getConfig().getString("ams.cfg.elot.path") %></p>
    <p><strong>ELOT Index Path</strong>: <%= app.getConfig().getString("ams.cfg.elotIndex.path") %></p>
    <p><strong>LACSLink Path</strong>: <%= app.getConfig().getString("ams.cfg.lacsLink.path") %></p>
    <p><strong>DPV Path</strong>: <%= app.getConfig().getString("ams.cfg.dpv.path") %></p>
    <p><strong>FNS Path</strong>: <%= app.getConfig().getString("ams.cfg.fns.path") %></p>
    <p><strong>SuiteLink Path</strong>: <%= app.getConfig().getString("ams.cfg.suiteLink.path") %></p>
    <p><strong>Abbreviated Street Path</strong>: <%= app.getConfig().getString("ams.cfg.abrst.path") %></p>
</body>
</html>
