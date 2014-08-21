# EPF File updater

## What is EPF?
Electronic Product Fulfillment (EPF) application is designed to provide subscription products to customers efficiently. As products become available, based on frequency (weekly, monthly, annually), they are posted to this secure site where customers can simply download the subscribed product files.

----
## Getting started
* rename epf_config-sample.ini.ini to epf_config.ini and start making changes


## Understanding the epf_config.ini sections

### debug
* Controll the verbosity of logging and set a file path to deposit into.
  * Verbosity is a range: Info prints a ton -> FATAL only prints when it fails.
  * If the 'log_file' doesn't exist messages will end up in the apache error log.

### credentials
* This is your login & password obtained by filling out [the usps.gov signup form]( http://ribbs.usps.gov/forms/documents/tech_guides/ps5116.pdf "usps.gov ps5116.pdf"), **this is not a free service**

### file
* Product Codes for AKAMAI Edge Server Files are retrieved by following the pattern shown in the sample config, codes are listed on [page 6](usps_epf_restapi_guide.pdf "usps epf restapi guide")

### epf
* Product Codes for EPF Web Server Files **Not yet implemented**, codes listed on [page 8](usps_epf_restapi_guide.pdf "usps epf restapi guide")

### destination
* File system location for script to deposit downloaded files, This path needs to exist or you won't be able to download anything.

## Gathering files with efp.php

* This script loops over all of the files within your config and download newest file that hasn't been downloaded yet. It also has the ability to overwrite exisiting files and also generate quite output. For more info check out the comments in epf.php


## example output

```
$ php ./epf.php
Server is online, Version v1.05.1
Login was Successful
Getting List for FILE Code:AMS, ID:DVD_COMM
List was Successful, Found 2 Items
Downloading Newest file: /epf/pepf/a00shared/epfdata/ams/20140915/dvd_comm.tar
File will be placed in: /data/downloads/20140915_dvd_comm.tar
Updated File status to 'Started'
Updated File status to 'Completed'
Completed FILE Code:AMS, ID:DVD_COMM
```
