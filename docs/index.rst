.. USPS AMS Web Service documentation master file, created by
   sphinx-quickstart on Thu Oct 24 10:57:37 2013.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to USPS AMS Web Service's documentation
===============================================

The AMS API provides methods for address correction, address inquiry, and city/state lookup.

There is currently no authentication required to use this API.

The API returns results in either JSON, JSONP, or XML. The default format is JSON. You can specify the output formats
by appending the query param `format` to the request url::

    JSON:  format=json
    JSONP: format=jsonp&callback=your_fn_name
    XML:   format=xml

API Methods
-----------

The API exposes the following methods:

+-------------+---------------------------------------------+
| Method      | Description                                 |
+=============+=============================================+
| validate    | Perform USPS address correction.            |
+-------------+---------------------------------------------+
| citystate   | Lookup the city and state given the zip5.   |
+-------------+---------------------------------------------+
| inquiry     | Lookup a matching address given a zip9.     |
+-------------+---------------------------------------------+
| info        | Get USPS AMS library information            |
+-------------+---------------------------------------------+

Validate
~~~~~~~~

This is the method you will most likely need to use. Given an input address it will try to perform a match against
USPS address records and return either a response indicating the correct form of the address or some detailed error
messages otherwise.

Query Params (Not all are required):

+-------------+---------------------------------------------+
| Param       | Description                                 |
+=============+=============================================+
| addr1       | Street address                              |
+-------------+---------------------------------------------+
| addr2       | Street address line 2                       |
+-------------+---------------------------------------------+
| city        | City name                                   |
+-------------+---------------------------------------------+
| state       | State name/abbreviation                     |
+-------------+---------------------------------------------+
| zip5        | 5 digit zip code                            |
+-------------+---------------------------------------------+
| zip4        | 4 digit zip code                            |
+-------------+---------------------------------------------+
| detail      | Show detailed information (true|false)      |
+-------------+---------------------------------------------+


Usage::

    /api/validate?addr1=200 State Street&city=Albany&state=NY   // Using addr1, city, state
    /api/validate?addr1=200 State Street&zip5=12210             // Using addr1, zip5

This method requires at minimum either::

    (addr1, city, state) or (addr1, zip5)

The usage above is the minimum requirement, thus you are allowed to fill all the query params
if the data is available.

By default the `detail` param is set to false. If set to true all the available address record
information will be returned.

Default Output (detail=false)::

    {
      "validated" : true,
      "address" : {
        "firm" : "",
        "addr1" : "200 STATE ST",
        "addr2" : "",
        "city" : "ALBANY",
        "state" : "NY",
        "zip5" : "12210",
        "zip4" : ""
      },
      "status" : {
        "code" : 31,
        "name" : "EXACT_MATCH",
        "desc" : "Single response based on input information."
      },
      "footnotes" : [ {
        "code" : "N",
        "name" : "Address Standardized",
        "desc" : "The delivery address was standardized. For example, if STREET was in the delivery address,
                  the system will return ST as its standard spelling. "
      } ]
    }

Detailed Output (detail=true)::

    {
      "validated" : true,
      "address" : {
        "firm" : "",
        "addr1" : "200 STATE ST",
        "addr2" : "",
        "city" : "ALBANY",
        "state" : "NY",
        "zip5" : "12210",
        "zip4" : ""
      },
      "status" : {
        "code" : 31,
        "name" : "EXACT_MATCH",
        "desc" : "Single response based on input information."
      },
      "footnotes" : [ {
        "code" : "N",
        "name" : "Address Standardized",
        "desc" : "The delivery address was standardized. For example, if STREET was in the delivery address,
                  the system will return ST as its standard spelling. "
      } ],
      "detail" : {
        "standardCityAbbr" : "",
        "postOfficeCity" : "ALBANY",
        "postOfficeState" : "NY",
        "deliveryBarCode" : "",
        "carrierRoute" : "C001",
        "fipsCounty" : 1
      },
      "recordCount" : 1,
      "records" : [ {
        "recordId" : 0,
        "recordType" : "S",
        "recordTypeDesc" : "Street Record",
        "primaryLow" : "200",
        "primaryHigh" : "238",
        "primaryParity" : "E",
        "preDir" : "",
        "streetName" : "STATE",
        "streetSuffix" : "ST",
        "postDir" : "",
        "unit" : "",
        "secondaryLow" : "",
        "secondaryHigh" : "",
        "secondaryParity" : " ",
        "zip5" : "12210",
        "zip4Low" : "2138",
        "zip4High" : "2138",
        "financeCode" : "350060",
        "fipsCounty" : "001"
      } ]
    }

The corrected address (or input address if match failed) is represented by the `address` portion of the output.

The following table lists all possible status codes for this method:

+-------+------------------------+-------------------------------------------------------------------------------------------------------+
| Code  |  Name                  |  Description                                                                                          |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  2    |  INSUFFICIENT_ADDRESS  |  The input address does not contain the required address components.                                  |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  10   |  INVALID_DUAL_ADDRESS  |  Information presented could not be processed in current format. Corrective action is needed.         |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  11   |  INVALID_CITY_ST_ZIP   |  The ZIP Code in the submitted address could not be found due to invalid city/st/zip.                 |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  12   |  INVALID_STATE         |  The state in the submitted address is invalid. Corrective action is needed.                          |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  13   |  INVALID_CITY          |  The city in the submitted address is invalid. Corrective action is needed.                           |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  21   |  NOT_FOUND             |  The address, exactly as submitted, could not be found in the national ZIP+4 file.                    |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  22   |  MULTI_RESPONSE        |  More than one ZIP+4 Code was found to satisfy the address submitted.                                 |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  31   |  EXACT_MATCH           |  Single response based on input information.                                                          |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+
|  32   |  DEFAULT_MATCH         |  A match was made to a default record in the national ZIP+4 file. A more specific match may be exist. |
+-------+------------------------+-------------------------------------------------------------------------------------------------------+

.. note:: The descriptions listed in the table are just summarized versions of the actual descriptions returned by the API.

The `standardCityAbbr` will be empty if the returned city is already abbreviated. Otherwise it will have the value of
the abbreviated city name.

The `footnotes` portion contains an array of messages that the matching system returns.

The `records` portion is an array of address record objects. Since the matching system primarily performs matching using
a database of address records, all matching records are returned in this listing. If an exact match was found this array
will usually contain one entry. In the event of ambiguity or multi-matches, all possible records will be listed. This can
be particularly useful when entering in the address of an apartment building and then viewing the valid unit ranges.

City/State
~~~~~~~~~~

Use this method if you want to find the city and state that a 5 digit zip code maps to.

Query Params:

+-------------+---------------------------------------------+
| Param       | Description                                 |
+=============+=============================================+
| zip5        | 5 digit zip code                            |
+-------------+---------------------------------------------+
| detail      | If true get full city details.              |
+-------------+---------------------------------------------+

Usage::

    /api/citystate?zip5=11716

This method offers additional information about the city such as the abbreviated form when the `detail=true` query param is set.

Default Output (detail=false)::

    {
    "success" : true,
    "cityName" : "BOHEMIA",
     "cityAbbr" : "",
     "zipCode" : "11716",
     "stateAbbr" : "NY"
    }

Detailed Output (detail=true)::

    {
    "success" : true,
     "cityName" : "BOHEMIA",
    "cityAbbr" : "",
    "zipCode" : "11716",
    "stateAbbr" : "NY",
     "zipClass" : {
       "code" : "B",
      "desc" : "Non-Unique Zip5"
    },
    "facility" : {
      "code" : "P",
      "desc" : "Post office"
    },
     "mailingNameInd" : "Y",
      "preferredCity" : "BOHEMIA",
      "countyNum" : "103",
      "countyName" : "SUFFOLK"
    }

The `cityAbbr` will be empty if the returned city is already abbreviated. Otherwise it will have the value of
the abbreviated city name.

The following table lists all possible zip class codes for this method:

+------+------------------------+
| Code |  Name                  |
+------+------------------------+
|  M   |  APO/FPO Military Zip5 |
+------+------------------------+
|  P   |  PO BOX Zip5           |
+------+------------------------+
|  U   |  Unique Zip5           |
+------+------------------------+
|  B   |  Non-Unique Zip5       |
+------+------------------------+

The following table lists all possible facility codes for this method:

+------+--------------------------+
| Code |  Name                    |
+------+--------------------------+
|  A   |  Airport mail facility   |
+------+--------------------------+
|  B   |  Branch                  |
+------+--------------------------+
|  C   |  Community post office   |
+------+--------------------------+
|  D   |  Area distrib. center    |
+------+--------------------------+
|  E   |  Sect. center facility   |
+------+--------------------------+
|  F   |  General distrib. center |
+------+--------------------------+
|  G   |  General mail facility   |
+------+--------------------------+
|  K   |  Bulk mail center        |
+------+--------------------------+
|  M   |  Money order unit        |
+------+--------------------------+
|  N   |  Non-postal name,        |
|      |  community name,         |
|      |  former postal facility, |
|      |  or place name           |
+------+--------------------------+
|  P   |  Post office             |
+------+--------------------------+
|  S   |  Station                 |
+------+--------------------------+
|  U   |  Urbanization            |
+------+--------------------------+

Inquiry
~~~~~~~

This method should be used if a full Zip 9 code is known and a matching address record is desired.
A Zip 9 code does not necessarily map to a single address so a multi-match status should typically be expected.

The available query param options are:

+-------------+---------------------------------------------+
| Param       | Description                                 |
+=============+=============================================+
| zip5        | 5 digit zip code                            |
+-------------+---------------------------------------------+
| zip4        | 4 digit zip code                            |
+-------------+---------------------------------------------+
| detail      | Show detailed information (true|false)      |
+-------------+---------------------------------------------+

Usage::

    /api/inquiry?zip5=12180&zip4=1928

The output is exactly identical to the validate method so it won't be reproduced here.

Info
~~~~

This method returns information about the USPS AMS library underlying the API. Specifically

- `apiVersion`
- `dataExpireDays`
- `libraryExpireDays`

There are no query parameters.

Usage::

    /api/info

Output::

    {
      "apiVersion" : "3.03.05.N",
      "dataExpireDays" : 103,
      "libraryExpireDays" : 529
    }

Batch API Methods
-----------------

The `validate`, `citystate`, and `inquiry` methods all support batch processing. The batch mode is triggered by
sending a POST request to the API method and appending the query param `batch=true` to the url. Any input params
(addr1, zip5, etc) appended to the url will be ignored.

The input list must be encoded as a JSON string and set as the POST body payload.

If the full information for each record is desired, add the `detail=true` query param to the url in the same way as you
would for single request.

While the input must be a JSON encoded payload, the output can be any of the supported formats and can be toggled using
the `format` query param in the url.

.. note:: According to the AMS documentation, the AMS library is intended to be used in single threaded applications.
          As a result it's not possible at this time to take advantage of multi-threaded batch operations.


Batch Validate
~~~~~~~~~~~~~~

Sample Usage::

    POST
    /api/validate?batch=true

    Sample JSON Input
    [ {"addr1":"75 Ridge Dr","city":"Manhasset","state":"NY","zip5":"11030"},
      {"addr1":"8 Parkway Dr","city":"Roslyn Heights","state":"NY","zip5":"11577"},
      {"addr1":"157 Evans St","city":"New Hyde Park","state":"NY","zip5":"11040"},
      {"addr1":"39 Irma Ave","city":"Port Washington","state":"NY","zip5":"11050"},
      {"addr1":"470 Tulip Ave","city":"Floral Park","state":"NY","zip5":"11001"} ]

The input objects in the list represent the same key value pairs as the query parameters used in the single request version.

Given the above request the output will be the following::

    {
      "results" : [ {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "75 RIDGE DR",
          "addr2" : "",
          "city" : "MANHASSET",
          "state" : "NY",
          "zip5" : "11030",
          "zip4" : "3115"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      }, {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "8 PARKWAY DR",
          "addr2" : "",
          "city" : "ROSLYN HEIGHTS",
          "state" : "NY",
          "zip5" : "11577",
          "zip4" : "2706"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      }, {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "157 EVANS ST",
          "addr2" : "",
          "city" : "NEW HYDE PARK",
          "state" : "NY",
          "zip5" : "11040",
          "zip4" : "1757"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      }, {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "39 IRMA AVE",
          "addr2" : "",
          "city" : "PORT WASHINGTON",
          "state" : "NY",
          "zip5" : "11050",
          "zip4" : "2811"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      }, {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "470 TULIP AVE",
          "addr2" : "",
          "city" : "FLORAL PARK",
          "state" : "NY",
          "zip5" : "11001",
          "zip4" : "3206"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      } ],
      "total" : 5
    }

Batch City/State
~~~~~~~~~~~~~~~~

Sample Usage::

    POST
    /api/citystate?batch=true

    POST Body
    ["11030","11577","11040","11050","11001"]

The input should be a single array of zip5 strings.

Sample Output::

    {
      "results" : [ {
        "success" : true,
        "cityName" : "MANHASSET",
        "cityAbbr" : "",
        "zipCode" : "11030",
        "stateAbbr" : "NY"
      }, {
        "success" : true,
        "cityName" : "ROSLYN HEIGHTS",
        "cityAbbr" : "ROSLYN HTS",
        "zipCode" : "11577",
        "stateAbbr" : "NY"
      }, {
        "success" : true,
        "cityName" : "NEW HYDE PARK",
        "cityAbbr" : "",
        "zipCode" : "11040",
        "stateAbbr" : "NY"
      }, {
        "success" : true,
        "cityName" : "PORT WASHINGTON",
        "cityAbbr" : "PRT WASHINGTN",
        "zipCode" : "11050",
        "stateAbbr" : "NY"
      }, {
        "success" : true,
        "cityName" : "FLORAL PARK",
        "cityAbbr" : "",
        "zipCode" : "11001",
        "stateAbbr" : "NY"
      } ],
      "total" : 5
    }

Batch Inquiry
~~~~~~~~~~~~~

Sample Usage::

    POST
    /api/inquiry?batch=true

    POST Body
    [{"zip5":"11030","zip4":"3115"},
     {"zip5":"11577","zip4":"2706"},
     {"zip5":"11040","zip4":"1757"},
     {"zip5":"11050","zip4":"2811"},
     {"zip5":"11001","zip4":"3206"}]

Sample Output::

    {
      "results" : [ {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "RIDGE DR",
          "addr2" : "",
          "city" : "MANHASSET",
          "state" : "NY",
          "zip5" : "11030",
          "zip4" : "3115"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      }, {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "PARKWAY DR",
          "addr2" : "",
          "city" : "ROSLYN HEIGHTS",
          "state" : "NY",
          "zip5" : "11577",
          "zip4" : "2706"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      }, {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "EVANS ST",
          "addr2" : "",
          "city" : "NEW HYDE PARK",
          "state" : "NY",
          "zip5" : "11040",
          "zip4" : "1757"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      }, {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "IRMA AVE",
          "addr2" : "",
          "city" : "PORT WASHINGTON",
          "state" : "NY",
          "zip5" : "11050",
          "zip4" : "2811"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      }, {
        "validated" : true,
        "address" : {
          "firm" : "",
          "addr1" : "TULIP AVE",
          "addr2" : "",
          "city" : "FLORAL PARK",
          "state" : "NY",
          "zip5" : "11001",
          "zip4" : "3206"
        },
        "status" : {
          "code" : 31,
          "name" : "EXACT_MATCH",
          "desc" : "Single response based on input information."
        },
        "footnotes" : [ ]
      } ],
      "total" : 5
    }

Contents:

.. toctree::
   :maxdepth: 2


Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`