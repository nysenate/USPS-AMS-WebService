USPS-AMS-WebService
===================

A Web Service that exposes the functionality of the USPS Address Matching System C library through an easy to use API and interface.

Prerequisites
-------------

1. Linux, JDK 8, Tomcat 8, Maven, GCC Compiler.

1.  Obtain the five data folders from the USPS AMS disk
    * ams_comm
    * ams_dpv
    * ams_elot
    * lacslink
    * suitelink

1. Have the necessary library files from the AMS disk (shared objects and headers).
   The filenames listed here are not exact.
    * libabbrst.so
    * libkeymgr.so
    * libz4lnx64.so
    * libdpv.so
    * libstelnk.so
    * zip4_lnx64.a
    * zip4.h
    * z4dpv.h

1. Have access to the date/time log file
    * z4cxlog.dat

Setup
-----

1. Place the data folders in */data/usps_ams*.

1. Place the library files in */opt/usps_ams*.

1. Edit the ldconf (/etc/ld.so.conf) and add an entry for */opt/usps_ams*.

1. Navigate to your src/main/c directory and run *make* to obtain *libamsnative.so*. If you
   are running into problems, edit the Makefile and ensure that the java paths are set correctly.

1. Tomcat must be able to load the *amsnative* shared library.
The *java.library.path* environment variable contains a listing of the search directories.
Either modify your Tomcat startup and set -Djava.library.path to the location of *libamsnative.so* or place
*libamsnative.so* into one of the default library folders that Tomcat looks in (e.g. /usr/lib/).

1. Create app.properties in /src/main/resources using app.example.properties as a template.

    * ams.cfg.system.path should point to the directory where the z4cxlog.dat file is located (/opt/usps_ams).
    * If you have stored the ams data folders in an alternate location adjust the other file paths accordingly.
    
Finally, perform *mvn compile war:war* and deploy .war to Tomcat.

Note: Due to loading of native libraries it's recommended to restart the Tomcat server when trying to do re-deployments.
