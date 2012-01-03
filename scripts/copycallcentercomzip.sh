#!/bin/ksh
#Mlamriben 10-21-2011
# This script copies /FTP's the com.zip file from the environment that it's executed on to the appropriate ftp location on the staging server. 
# xpedx BIM can then download the zip file and place on the share/S drive accessible to all BIMs
ENVIRONMENT
#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpaappmc01) ENVIRONMENT=mc;; 
 *);;
esac
echo "Started ftp of Call Center com.zip file to $HOST_NAME on /xpedx/sterling/share/callcenter/$ENVIRONMENT"

ftp -n xpappt01 <<END_SCRIPT
quote USER xpadmin
quote PASS xpAdm1n!
lcd /xpedx/sterling/Foundation/rcpdrop/windows/9.0/
cd /xpedx/sterling/share/callcenter/$ENVIRONMENT
put com.zip
quit
END_SCRIPT 	



