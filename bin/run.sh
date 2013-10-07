#!/bin/bash
source $(dirname "$0")/utils.sh

BASE="/usr/share/tomcat7/webapps/USPS/WEB-INF"

case $1 in
    --help | -h | help) echo "USAGE: `basename $0` SCRIPT_CLASS_NAME|help <args>"; exit;;
esac

# TODO: This memory size should be an adjustable parameter
java -Xmx1024m -Xms16m -Djava.library.path=/home/ash/Web/nysenate/USPS-AMS-WebService/src/main/c -cp $BASE/classes/:$BASE/lib/* gov.nysenate.ams.script.$@
