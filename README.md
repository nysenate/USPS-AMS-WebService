USPS-AMS-WebService
===================

A network wrapper around the USPS Address Matching System API library that utilizes JNI to connect a Java servlet with the AMS proprietary C library.

Setup
=====

Prerequisites:
1. Obtain the five data folders from the AMS disk.
	ams_comm; ams_dpv; ams_elot; lacslink; suitelink

2. Have the necessary library files from the ams disk(shared objects).
	libabbrst.so.1; libkeymgr.so.3; libz4lnx64.so;
	zip4.h; libdpv.so.8; libstelnk.so.1;
	z4dpv.h; zip4_lnx64.a; z4cxlog.dat

3. Place the library files in the directory /opt/usps_ams

4. Navigate to your src/main/c directory and run make to obtain            libamsnative.so

5. Copy over the shared library to /usr/lib/libamsnative.so

6. Create app.properties in /src/main/resources using app.example.properties as a template

7. ams.cfg.system.path should point to /opt/usps_ams more specifcally the directy where the z4cxlog.dat fileis located

8. If you have stored the ams data folders in an alternate location ajust the other file paths accordingly

8. Do a maven compile war:war and then deploy to tomcat 


