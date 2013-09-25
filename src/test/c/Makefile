#
# Project: USPS-AMS-WebService
# Author: Ken Zalewski
# Organization: New York State Senate
#

TARGETS= ams

ams: ams.c config.c tcp.c
	$(CC) -Wall -I/opt/usps_ams -L/opt/usps_ams -lz4lnx64 $+ -o $@

clean:
	rm -f $(TARGETS)
