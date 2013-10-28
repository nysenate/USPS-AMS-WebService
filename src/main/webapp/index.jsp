<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html ng-app='ams' id='ng-app' class='ng-scope'>
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
    <script>window.contextPath = "${pageContext.request.contextPath}";</script>
    <script src="js/vendor/modernizr-2.6.2.min.js"></script>
</head>
<body>
    <!--[if lt IE 7]>
    <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
    <![endif]-->

    <div style="width:100%" id="header" ng-controller='MenuController'>
        <div id="uspsLogoText" style="float:left;margin-left:20px;width:120px;">
            <a>USPS AMS</a>
        </div>
        <ul class="top-method-header">
            <li>
                <a class="active">Address Correction</a>
                <a>API Documentation</a>
            </li>
        </ul>
    </div>

    <section ng-controller='ValidateController'>
        <div class='grid-3' style='margin:60px 0 0 40px;'>
            <div class='col-1' style='width:280px;'>
                <form method='post' ng-submit='lookup()' autocomplete="off">
                    <div id='address-input-container' style='height:auto;'>
                        <h3 class='section-title' style='background:#005588;color:white;'>Enter an address to validate</h3>
                        <hr class='section-title-hr'/>
                        <ul class='address-input-list'>
                            <li>
                                <label>Address Line 1</label>
                                <input ng-model='addr1' type='text' id='addr1-input'/>
                            </li>
                            <li>
                                <label>Address Line 2</label>
                                <input ng-model='addr2' type='text' id='addr2-input'/>
                            </li>
                            <li>
                                <label>City</label>
                                <input ng-model='city' type='text' id='city-input'/>
                            </li>
                            <li>
                                <label>State</label>
                                <select ng-model='state' name="state">
                                    <option value="AL">Alabama</option>
                                    <option value="AK">Alaska</option>
                                    <option value="AZ">Arizona</option>
                                    <option value="AR">Arkansas</option>
                                    <option value="CA">California</option>
                                    <option value="CO">Colorado</option>
                                    <option value="CT">Connecticut</option>
                                    <option value="DE">Delaware</option>
                                    <option value="DC">District of Columbia</option>
                                    <option value="FL">Florida</option>
                                    <option value="GA">Georgia</option>
                                    <option value="HI">Hawaii</option>
                                    <option value="ID">Idaho</option>
                                    <option value="IL">Illinois</option>
                                    <option value="IN">Indiana</option>
                                    <option value="IA">Iowa</option>
                                    <option value="KS">Kansas</option>
                                    <option value="KY">Kentucky</option>
                                    <option value="LA">Louisiana</option>
                                    <option value="ME">Maine</option>
                                    <option value="MD">Maryland</option>
                                    <option value="MA">Massachusetts</option>
                                    <option value="MI">Michigan</option>
                                    <option value="MN">Minnesota</option>
                                    <option value="MS">Mississippi</option>
                                    <option value="MO">Missouri</option>
                                    <option value="MT">Montana</option>
                                    <option value="NE">Nebraska</option>
                                    <option value="NV">Nevada</option>
                                    <option value="NH">New Hampshire</option>
                                    <option value="NJ">New Jersey</option>
                                    <option value="NM">New Mexico</option>
                                    <option selected="selected" value="NY">New York</option>
                                    <option value="NC">North Carolina</option>
                                    <option value="ND">North Dakota</option>
                                    <option value="OH">Ohio</option>
                                    <option value="OK">Oklahoma</option>
                                    <option value="OR">Oregon</option>
                                    <option value="PA">Pennsylvania</option>
                                    <option value="RI">Rhode Island</option>
                                    <option value="SC">South Carolina</option>
                                    <option value="SD">South Dakota</option>
                                    <option value="TN">Tennessee</option>
                                    <option value="TX">Texas</option>
                                    <option value="UT">Utah</option>
                                    <option value="VT">Vermont</option>
                                    <option value="VA">Virginia</option>
                                    <option value="WA">Washington</option>
                                    <option value="WV">West Virginia</option>
                                    <option value="WI">Wisconsin</option>
                                    <option value="WY">Wyoming</option>
                                </select>
                            </li>
                            <li>
                                <label>Zip 5</label>
                                <input ng-model='zip5' type='text' id='zip5-input'/>
                            </li>
                            <li>
                                <button ng-click='lookup()' style='margin-top:10px;' class='submit' >Validate Address</button>
                                <!--<input type="submit" style='display:none;visibility: hidden;'/>-->
                            </li>
                        </ul>
                    </div>
                </form>
            </div>

            <div ng-show='result' class='col-2'>
                <div id='validated-address-container' style='width:100%;float:left;height:auto;'>
                    <h3 class='section-title success-indication'>{{result.status.code | statusNameFilter}}</h3>
                    <hr class='section-title-hr'/>
                    <div class='section-row' style='font-size:13px;'>
                        <p>{{result.status.desc}}</p>
                    </div>
                    <hr/>
                    <div class='section-row' style='font-size:16px;color:#333;'>
                        <p>{{result.address.addr1}}<br/>
                           {{result.address.city}}, {{result.address.state}} {{result.address.zip5}}
                            <span ng-show="result.address.zip4">-{{result.address.zip4}}</span></p>
                    </div>
                    <hr/>
                    <div class='section-row' style='font-size:13px;color:#333;'>
                        <p><span style='color:teal;font-weight:bold;'>City Abbreviation:</span> {{result.detail.standardCityAbbr || result.address.city}}</p>
                        <p><span style='color:teal;font-weight:bold;'>FIPS County:</span> {{result.detail.fipsCounty}}</p>
                        <p><span style='color:teal;font-weight:bold;'>PO Location:</span> {{result.detail.postOfficeCity}} {{result.detail.postOfficeState}}</p>
                    </div>
                </div>

                <div ng-show='result.footnotes.length > 0' id='footnotes-container' style='float:left;width:100%;'>
                    <h3 class='section-title warning-indication'>Footnotes</h3>
                    <hr class='section-title-hr'/>
                    <div class='section-row' style='font-size:13px;'>
                        <div ng-repeat='footnote in result.footnotes'>
                            <p style='color:teal;'><strong>{{footnote.name}}</strong></p>
                            <p>{{footnote.desc}}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class='col-3' style='margin-left:20px;min-width:400px;'>
                <div id='address-records-container'>
                    <h3 class='section-title'>Matched Address Records</h3>
                    <hr class='section-title-hr'/>
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th colspan="3">Primary</th>
                            <th colspan="2">Street</th>
                            <th colspan="2">Directional</th>
                            <th colspan="4">Secondary</th>
                            <th colspan="3">Postal</th>
                        </tr>
                        <tr>
                            <th></th>
                            <th>Low</th>
                            <th>High</th>
                            <th>Parity</th>
                            <th>Name</th>
                            <th>Suffix</th>
                            <th>Pre</th>
                            <th>Post</th>
                            <th>Unit</th>
                            <th>Low</th>
                            <th>High</th>
                            <th>Parity</th>
                            <th>Zip5</th>
                            <th>Zip4 Low</th>
                            <th>Zip4 High</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </section>
    <section style='display:none'>
        <iframe style='border:0;outline:0;width:100%;height:100%;' src="${pageContext.request.contextPath}/docs/_build/html/index.html"></iframe>
    </section>

    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="${pageContext.request.contextPath}/js/vendor/jquery-1.10.1.min.js"><\/script>')</script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.8/angular.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/common.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/app.js" type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/js/plugins.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>