#
# Project: USPS-AMS-WebService
# Organization: New York State Senate
#

TARGETS= libamsnative.so

libamsnative.so: AmsNativeDao.c
	$(CC) -shared -fPIC -o $@ -I/usr/lib/jvm/jdk1.7.0_21/include -I/usr/lib/jvm/jdk1.7.0_21/include/linux -I/opt/usps_ams -Wall $+ -L/opt/usps_ams -lz4lnx64

clean:
	rm -rf libamsnative.so

