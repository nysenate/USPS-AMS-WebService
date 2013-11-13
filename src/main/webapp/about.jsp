<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="gov.nysenate.ams.util.Application" %>
<html>
<head>
    <title>AMS License Info</title>
</head>
<body>
    <h3>AMS License Info</h3>
    <hr/>
    <p>Api Version: <%= Application.getAmsNativeProvider().getApiVersion() %></p>
    <p>Days Until Data Expires: <%= Application.getAmsNativeProvider().getDataExpireDays() %></p>
    <p>Days Until Library Expires: <%= Application.getAmsNativeProvider().getLibraryExpireDays() %></p>
    <br/>
    <h3>Environment Info</h3>
    <hr/>
    <p><strong>Java Library Path</strong>: <%= System.getProperty("java.library.path") %></p>
    <p><strong>AMS System Path</strong>: <%= Application.getConfig().getValue("ams.cfg.system.path") %></p>
    <p><strong>Address 1 Path</strong>: <%= Application.getConfig().getValue("ams.cfg.address1.path") %></p>
    <p><strong>Address Index Path</strong>: <%= Application.getConfig().getValue("ams.cfg.addrindex.path") %></p>
    <p><strong>City/State Path</strong>: <%= Application.getConfig().getValue("ams.cfg.citystate.path") %></p>
    <p><strong>Cross Ref Path</strong>: <%= Application.getConfig().getValue("ams.cfg.crossref.path") %></p>
    <p><strong>ELOT Path</strong>: <%= Application.getConfig().getValue("ams.cfg.elot.path") %></p>
    <p><strong>ELOT Index Path</strong>: <%= Application.getConfig().getValue("ams.cfg.elotindex.path") %></p>
    <p><strong>LACSLink Path</strong>: <%= Application.getConfig().getValue("ams.cfg.lacslink.path") %></p>
    <p><strong>DPV Path</strong>: <%= Application.getConfig().getValue("ams.cfg.dpv.path") %></p>
    <p><strong>FNS Path</strong>: <%= Application.getConfig().getValue("ams.cfg.fns.path") %></p>
    <p><strong>SuiteLink Path</strong>: <%= Application.getConfig().getValue("ams.cfg.suitelink.path") %></p>
    <p><strong>Abbreviated Street Path</strong>: <%= Application.getConfig().getValue("ams.cfg.abrst.path") %></p>
</body>
</html>