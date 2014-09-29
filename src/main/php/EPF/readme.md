# EPF File updater

## What is EPF?
Electronic Product Fulfillment (EPF) is designed to provide subscription-based products to USPS customers efficiently.  As products become available, based on frequency (weekly, monthly, annually), they are posted to this secure site where customers can simply download the subscribed product files.

----
## Getting started
* Rename epf_config-sample.ini to epf_config.ini and start making changes.


## Understanding the epf_config.ini sections

### debug
* Controls the verbosity of logging and sets a file path to store the log file.
  * Verbosity is a range: INFO prints a ton -> FATAL only prints when it fails.
  * If the 'log_file' doesn't exist, messages end up in the Apache error log.

### credentials
* Specifies your username and password obtained by filling out [the usps.gov signup form]( http://ribbs.usps.gov/forms/documents/tech_guides/ps5116.pdf "usps.gov ps5116.pdf"), **this is not a free service**

### file
* Product Codes for AKAMAI Edge Server Files are retrieved by following the pattern shown in the sample config; codes are listed on [page 6](usps_epf_restapi_guide.pdf "usps epf restapi guide")

### epf
* Product Codes for EPF Web Server Files **Not yet implemented**; codes are listed on [page 8](usps_epf_restapi_guide.pdf "usps epf restapi guide")

### destination
* File system location for script to store downloaded files. This path needs to exist or you won't be able to download anything.

## Gathering files with epf.php

* This script loops over all of the files specified in the config and downloads the newest file that hasn't been downloaded yet.  It also has the ability to overwrite existing files and to generate quiet output.  For more info, check out the comments in epf.php.


## Example Output

```
$ php ./epf.php
Server is online, Version v1.05.1
Login was Successful
Getting List for FILE Code:AMS, ID:DVD_COMM
List was Successful, Found 2 Items
Downloading most recent file: /epf/pepf/a00shared/epfdata/ams/20140915/dvd_comm.tar
File will be placed in: /data/downloads/20140915_dvd_comm.tar
Updated File status to 'Started'
Updated File status to 'Completed'
Completed FILE Code:AMS, ID:DVD_COMM
```

