.. USPS AMS Web Service documentation master file, created by
   sphinx-quickstart on Thu Oct 24 10:57:37 2013.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to USPS AMS Web Service's documentation
===============================================

The AMS API provides methods for address correction, address inquiry, and city/state lookup.

There is currently no authentication needed to use this API.

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

Validate
~~~~~~~~

Query Params:

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

As shown in the usage above this method requires at minimum either::

    (addr1, city, state) or (addr1, zip5)

The usage above is the minimum requirement, thus you are allowed to fill all the query params
if desired.

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

The `footnotes` portion contains an array of messages that the matching system returns. The full listing of footnotes
can be found at the end of this page.

The `records` portion is an array of address record objects. Since the matching system primarily performs matching using
a database of address records, all matching records are returned in this listing. If an exact match was found this array
will usually contain one entry. In the event of ambiguity or multi-matches, all possible records will be listed.

City/State
----------

Query Params:

+-------------+---------------------------------------------+
| Param       | Description                                 |
+=============+=============================================+
| zip5        | 5 digit zip code                            |
+-------------+---------------------------------------------+

Usage::

    /api/citystate?zip5=11716

As shown in the usage above this method requires::

    (zip5)

By default the `detail` param is set to false. If set to true all the available address record
information will be returned.

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

Default Output (detail=false)::

    {
    "success" : true,
    "cityName" : "BOHEMIA",
     "cityAbbr" : "",
     "zipCode" : "11716",
     "stateAbbr" : "NY"
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
|      |   community name,        |
|      |   former postal facility,|
|      |   or place name          |
+------+--------------------------+
|  P   |  Post office             |
+------+--------------------------+
|  S   |  Station                 |
+------+--------------------------+
|  U   |  Urbanization            |
+------+--------------------------+

Appendix
--------

Footnotes
~~~~~~~~~

Contents:

.. toctree::
   :maxdepth: 2


Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`