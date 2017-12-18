# EPF File updater

## What is EPF?
Electronic Product Fulfillment (EPF) is designed to provide subscription-based products to USPS customers efficiently.  As products become available, based on frequency (weekly, monthly, annually), they are posted to this secure site where customers can simply download the subscribed product files.

----
## Getting started
* Rename epf_config-sample.ini to epf_config.ini and start making changes.
* The sample config file contains all of the parameters that can be changed.
* The parameters include:
  * log_level - FATAL, ERROR, WARN, INFO, DEBUG
  * log_file - location of the logging output file
  * api_url - base URL for all EPF API calls
  * username - username associated with USPS account (AMS API is not free)
  * password - password associated with USPS account
  * output_dir - directory where downloaded files will be saved

## Gathering files with epf.php

* This script iterates over all of the EPF files that are available to the
specified user and downloads the newest file that hasn't been downloaded yet.  It also has the ability to overwrite existing files and to generate quiet output.  For more info, use the "--help" command line option.

## Example Output

```
$ php epf.php
2017-12-18:12:58:30 [INFO] USPS EPF server version: v1.7.1
2017-12-18:12:58:30 [INFO] USPS EPF build date: 2017-01-30
2017-12-18:12:58:31 [INFO] Retrieved full file list; count=6
2017-12-18:12:58:31 [INFO] File count: 6; last fulfilled timestamp: 2018-01-15
2017-12-18:12:58:31 [INFO] About to download file '/epf/pepf/a00shared/epfdata/amsx/20180115/ams_devkit.tar' (id=3800266, status='C', product=AMSX:AMSDK)
2017-12-18:12:58:31 [WARN] Download file '/epf/pepf/a00shared/epfdata/amsx/20180115/ams_devkit.tar' (id=3800266, status='C', product=AMSX:AMSDK) already exists as '/tmp/20180115_ams_devkit.tar'; skipping
2017-12-18:12:58:31 [INFO] About to download file '/epf/pepf/a00shared/epfdata/amsx/20180115/dpv_devkit.tar' (id=3800255, status='C', product=AMSX:DPVDK)
2017-12-18:12:58:31 [WARN] Download file '/epf/pepf/a00shared/epfdata/amsx/20180115/dpv_devkit.tar' (id=3800255, status='C', product=AMSX:DPVDK) already exists as '/tmp/20180115_dpv_devkit.tar'; skipping
2017-12-18:12:58:31 [INFO] About to download file '/epf/pepf/a00shared/epfdata/amsx/20180115/dvd_comm.tar' (id=3800267, status='C', product=AMSX:DVD_COMM)
2017-12-18:12:58:31 [INFO] Current file '/epf/pepf/a00shared/epfdata/amsx/20180115/dvd_comm.tar' will be saved as '/tmp/20180115_dvd_comm.tar'
2017-12-18:12:58:32 [INFO] Updated file status for file id=3800267 to 'S'
2017-12-18:12:58:37 [INFO] Downloaded 3.6 mb in 5 seconds
2017-12-18:12:58:42 [INFO] Downloaded 6.4 mb in 10 seconds
   .
   .
   .
```

