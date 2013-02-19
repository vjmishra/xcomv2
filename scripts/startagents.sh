#!/bin/ksh
# This script starts the agents

#cd
#cd /xpedx/sterling/Foundation/bin

DATE=`date +%Y-%m-%d`
HOST_NAME="$(hostname)"
echo "Starting agents on $HOST_NAME"
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=sandbox;;
 zxpappt01) ENVIRONMENT=stg;; 
 zxpapps01) ENVIRONMENT=ps;; 
 zxpagnt01) ENVIRONMENT=prod01;; 
 zxpagnt02) ENVIRONMENT=prod02;; 
 zxpappmc01) ENVIRONMENT=masterconfig;; 
 
 *);;
esac

#start the order updates 
cd /home/share/xpadmin/scripts/
ksh -x startorderupdates.sh
 
echo "calling startagents$ENVIRONMENT.sh"
ksh -x startagents$ENVIRONMENT.sh


