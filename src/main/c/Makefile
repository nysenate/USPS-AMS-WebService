#
# Project: USPS-AMS-WebService
# Organization: New York State Senate
#

TARGETS= libamsnative.so

libamsnative.so: AmsNativeDao.c
	$(CC) -shared -fPIC -o $@ -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux -I/opt/usps_ams -Wall $+ -L/opt/usps_ams -lz4lnx64

clean:
	rm -rf libamsnative.so

