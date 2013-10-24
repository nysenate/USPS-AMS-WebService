<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title></title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">

    <script src="js/vendor/modernizr-2.6.2.min.js"></script>
</head>
<body>
    <!--[if lt IE 7]>
    <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
    <![endif]-->

    <div style="width:100%" id="header">
        <div id="uspsLogoText" style="float:left;margin-left:20px;width:120px;">
            <a>USPS AMS</a>
        </div>
        <ul class="top-method-header">
            <li>
                <a class="active">Address Correction</a>
            </li>
        </ul>
        ${ap}
    </div>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="${pageContext.request.contextPath}/js/vendor/jquery-1.10.1.min.js"><\/script>')</script>

<script src="${pageContext.request.contextPath}/js/plugins.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>