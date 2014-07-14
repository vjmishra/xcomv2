#!/bin/ksh
# This script starts the order updates coming from MAX/WM
# created on 9/28/2012 by mahmoud L.

#cd
#cd /xpedx/sterling/Foundation/bin

DATE=`date +%Y-%m-%d`
HOST_NAME="$(hostname)"
echo "Starting order updates"
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev-fake;;
 zxpappt01) ENVIRONMENT=stg;; 
 zxpapps01) ENVIRONMENT=stg-fake;; 
 zxpappint01) ENVIRONMENT=integration;;
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpapp01) ENVIRONMENT=prod;; 
 zxpapp02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=dev-fake;; 
 *);;
esac

echo "calling startorderupdates$ENVIRONMENT.sh"
cd /home/share/xpadmin/scripts/orderupdateserver/
ksh -x oustart$ENVIRONMENT.ftp
echo "The order update start signal/file has been ftp'd to the remote server. It may take up to 15 minutes for the file to be picked up 
	by webmethods. Logon to the ftp.ipaper.com site and check if the oustart file has been picked up"
