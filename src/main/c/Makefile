#
# Project: USPS-AMS-WebService
# Organization: New York State Senate
#

LIBDIR= /usr/lib64
TARGETS= libamsnative.so

libamsnative.so: AmsNativeDao.c
	$(CC) -shared -fPIC -o $@ -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -I/opt/usps_ams -Wall $+ -L/opt/usps_ams -lz4lnx64

install: $(TARGETS)
	cp $(TARGETS) $(LIBDIR)
	@echo "Remember to run ldconfig to re-cache the libs."

clean:
	rm -rf libamsnative.so

