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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui-1.10.3.custom.css">
    <script>window.contextPath = "${pageContext.request.contextPath}";</script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/vendor/jquery-ui-1.10.3.custom.min.js"></script>
    <script>window.jQuery || document.write('<script src="${pageContext.request.contextPath}/js/vendor/jquery-1.10.1.min.js"><\/script>')</script>
    <script src="${pageContext.request.contextPath}/js/vendor/modernizr-2.6.2.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.8/angular.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/app.js"></script>
</head>
<body>
    <!--[if lt IE 7]>
    <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
    <![endif]-->

    <div id='wrapper'>
        <section class='api-section' ng-controller='ApiController' ng-cloak>
            <div class='grid-3'>
                <div id='api-input-container' class='col-1'>
                    <h3 class='section-title' style='padding-top:0px;background:#005588;color:white;'>
                        <img style='position:relative;top:10px;' height="30px" src="${pageContext.request.contextPath}/img/uspslogo.png"/>
                    </h3>
                    <hr class='section-title-hr'/>
                    <div id='method-selection'>
                        <input type='radio' name='method' id='validate-method-radio' checked value='validate' ng-model='activeRequestView'/>
                        <label for='validate-method-radio'>Validate</label>
                        <input type='radio' name='method' id='citystate-method-radio' value='cityState' ng-model='activeRequestView'/>
                        <label for='citystate-method-radio'>City/State</label>
                        <input type='radio' name='method' id='inquiry-method-radio' value='inquiry' ng-model='activeRequestView'/>
                        <label for='inquiry-method-radio'>Zip 9</label>
                    </div>
                    <div ng-show="activeRequestView == 'validate'">
                        <form method='post' ng-submit='sendValidateRequest()' autocomplete="off">
                            <div id='address-input-container' style='height:auto;'>
                                <div class='f13px-333' style='margin:20px 10px 0 10px'>
                                    Enter an address below to retrieve the USPS corrected version. In order to obtain a match Address Line 1 must be supplied with either City/State or Zip5.
                                </div>
                                <ul class='input-list'>
                                    <li>
                                        <label>Address Line 1</label>
                                        <input ng-model='validateInput.addr1' type='text'/>
                                    </li>
                                    <li>
                                        <label>Address Line 2</label>
                                        <input ng-model='validateInput.addr2' type='text'/>
                                    </li>
                                    <li>
                                        <label>City</label>
                                        <input ng-model='validateInput.city' type='text'/>
                                    </li>
                                    <li>
                                        <label>State</label>
                                        <select ng-model='validateInput.state' name="state">
                                            <option value=""></option>
                                            <option value="AA">U.S. Armed Forces - Americas</option>
                                            <option value="AE">U.S. Armed Forces - Europe</option>
                                            <option value="AP">U.S. Armed Forces - Pacific</option>
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
                                            <option value="PR">Puerto Rico</option>
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
                                        <input ng-model='validateInput.zip5' maxlength="5" type='text'/>
                                    </li>
                                    <li>
                                        <button ng-click='sendValidateRequest()' style='margin-top:10px;' class='submit' >Validate Address</button>
                                    </li>
                                </ul>
                            </div>
                        </form>
                    </div>
                    <div ng-show="activeRequestView == 'cityState'">
                        <form method='post' ng-submit='sendCityStateRequest()' autocomplete="off">
                            <div class='f13px-333' style='margin:20px 10px 0 10px'>
                                Enter a Zip 5 to retrieve the corresponding city and state of that Zip.<br/><br/>
                                </div>
                            <ul class='input-list'>
                                <li>
                                    <label>Zip 5</label>
                                    <input ng-model='cityStateInput.zip5' maxlength='5' type='text' />
                                </li>
                                <li>
                                    <button ng-click='sendCityStateRequest()' style='margin-top:10px;' class='submit'>City State Inquiry</button>
                                </li>
                            </ul>
                        </form>
                    </div>
                    <div ng-show="activeRequestView == 'inquiry'">
                        <form method='post' ng-submit='sendInquiryRequest()' autocomplete="off">
                            <div class='f13px-333' style='margin:20px 10px 0 10px'>
                                Enter a Zip 9 code to retrieve matching address records.<br/><br/>
                                Note that the Zip 9 code does not necessarily map to a specific address.
                            </div>
                            <ul class='input-list'>
                                <li>
                                    <label>Zip 5</label>
                                    <input ng-model='inquiryInput.zip5' maxlength="5" type='text'/>
                                </li>
                                <li>
                                    <label>Zip 4</label>
                                    <input ng-model='inquiryInput.zip4' maxlength="4" type='text'/>
                                </li>
                                <li>
                                    <button ng-click='sendInquiryRequest()' style='margin-top:10px;' class='submit'>Zip 9 Inquiry</button>
                                </li>
                            </ul>
                        </form>
                    </div>
                </div>

                <div id='api-response-container' class='col-2 animated' ng-show='responseVisible()'>
                    <div ng-controller='CityStateResponseController' ng-show="(activeResponseView ==  'cityState')">
                        <h3 class='section-title' ng-class='statusClass'>{{messageResponse}}</h3>
                        <hr class='section-title-hr'/>
                        <div class='section-row f13px-333' style='padding-right:20px;'>
                            <p>{{message}}</p>
                        </div>
                        <div ng-show='result.success'>
                            <hr/>
                            <div class='section-row f16px-333'>
                                <p>{{result.cityName}}, {{result.stateAbbr}}</p>
                            </div>
                            <hr/>
                            <div class='section-row f13px-333'>
                                <p><label class='dd'>City Name:</label> {{result.cityName}}</p>
                                <p><label class='dd'>City Abbreviation:</label> {{result.cityAbbr}}</p>
                                <p><label class='dd'>Zipcode:</label> {{result.zipCode}}</p>
                                <p><label class='dd'>State Abbreviation:</label> {{result.stateAbbr}}</p>
                                <p><label class='dd'>Mailing Name Indicator:</label> {{result.mailingNameInd}}</p>
                                <p><label class='dd'>Preferred City:</label> {{result.preferredCity}}</p>
                                <p><label class='dd'>County Name:</label> {{result.countyName}}</p>
                                <p><label class='dd'>Zip Class Code:</label> {{result.zipClass.desc}}</p>
                                <p><label class='dd'>Facility Code:</label> {{result.facility.desc}}</p>
                            </div>
                        </div>
                    </div>

                    <div ng-controller='ValidateResponseController'
                         ng-show="(activeResponseView == 'validate') || (activeResponseView == 'inquiry')">
                        <h3 class='section-title' ng-class='statusClass'>{{result.status.code | statusNameFilter}}</h3>
                        <hr class='section-title-hr'/>
                        <div class='section-row f13px-333' style='padding-right:20px;'>
                            <p>{{result.status.desc}}</p>
                        </div>
                        <hr/>
                        <div ng-show='result.validated'>
                            <div class='section-row f16px-333'>
                                <p>{{result.address.addr1}}<br/>
                                    {{result.address.city}}, {{result.address.state}} {{result.address.zip5}}<span ng-show="result.address.zip4">-{{result.address.zip4}}</span>
                                </p>
                            </div>
                            <hr/>
                        </div>
                        <div ng-show='result.validated'>
                            <div class='section-row f13px-333'>
                                <p><label class='dd'>City Abbreviation:</label> {{result.detail.standardCityAbbr || result.address.city}}</p>
                                <p><label class='dd'>FIPS County:</label> {{result.detail.fipsCounty}}</p>
                                <p><label class='dd'>PO Location:</label> {{result.detail.postOfficeCity}} {{result.detail.postOfficeState}}</p>
                                <p><label class='dd'>Carrier Route:</label> {{result.detail.carrierRoute}}</p>
                                <p><label class='dd'>Delivery Point Bar Code:</label> {{result.detail.deliveryBarCode}}</p>
                            </div>
                            <hr/>
                        </div>
                        <div ng-show='result.footnotes.length > 0'>
                            <div class='section-row' style='font-size:13px;'>
                                <div ng-repeat='footnote in result.footnotes'>
                                    <p style='color:#ff4500'><strong>{{footnote.name}}</strong></p>
                                    <p>{{footnote.desc}}</p>
                                </div>
                            </div>
                            <hr/>
                        </div>
                        <div id='address-records-container' ng-show='result.recordCount > 0'>
                            <div class='section-row'>
                                <p><span style='font-size:13px;font-weight:bold;color:#058;'>Matching Address Records</span></p>
                            </div>
                            <table class='light-table'>
                                <thead>
                                <tr>
                                    <th colspan="2">Record</th>
                                    <th colspan="3">Building</th>
                                    <th colspan="4">Street</th>
                                    <th colspan="4">Secondary</th>
                                    <th colspan="1">Zip5</th>
                                    <th colspan="2">Zip4</th>
                                </tr>
                                <tr>
                                    <th>ID</th>
                                    <th>Type</th>
                                    <th>Low</th>
                                    <th>High</th>
                                    <th>Parity</th>
                                    <th>Pre</th>
                                    <th>Name</th>
                                    <th>Suffix</th>
                                    <th>Post</th>
                                    <th>Unit</th>
                                    <th>Low</th>
                                    <th>High</th>
                                    <th>Parity</th>
                                    <th></th>
                                    <th>Low</th>
                                    <th>High</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat='record in result.records'>
                                    <td>{{record.recordId}}</td>
                                    <td>{{record.recordType}}</td>
                                    <td>{{record.primaryLow}}</td>
                                    <td>{{record.primaryHigh}}</td>
                                    <td>{{record.primaryParity | parityFilter}}</td>
                                    <td>{{record.preDir}}</td>
                                    <td>{{record.streetName}}</td>
                                    <td>{{record.streetSuffix}}</td>
                                    <td>{{record.postDir}}</td>
                                    <td>{{record.unit}}</td>
                                    <td>{{record.secondaryLow}}</td>
                                    <td>{{record.secondaryHigh}}</td>
                                    <td>{{record.secondaryParity | parityFilter}}</td>
                                    <td>{{record.zip5}}</td>
                                    <td>{{record.zip4Low}}</td>
                                    <td>{{record.zip4High}}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</body>
</html>